using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using Amazon.S3;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using Swerea_API.Models;
using Newtonsoft.Json.Serialization;
using Newtonsoft.Json;
using Swerea_API.utils;
using System.IO;
using Amazon.S3.Transfer;

namespace Swerea_API.Controllers
{
    [RoutePrefix("Build")]
    public class BuildController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();
        
        // GET: api/Build
        [Route("GetAllBuild")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetAllBuild()
        {
            // Retrieving list of builds 
            var builds = db.Build.Select(x => new { x.BuildID, x.QR_code, x.MaterialId, x.Status }).ToList();
            string json = JsonConvert.SerializeObject(builds, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }


        //TODO
        [HttpGet]
        [Route("BuildExist")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult BuildExist(string qrcode)
        {
            if (qrcode == null || qrcode == "")
            {
                return BadRequest();
            }

            try
            {
                var build = db.Build.FirstOrDefault(x => x.QR_code == qrcode);

                if (build == null)
                {
                    return Ok(false);
                }
                return Ok(true);
            }

            catch (InvalidOperationException e)
            {
                return Ok(false);
            }
        }

        // GET: api/Build/GetBuild/qr_code 
        [HttpGet]
        [Route("GetBuild")]
        [ResponseType(typeof(Build))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetBuild(String qrcode)
        {
            var buildDetails = db.Build
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (buildDetails == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error, QR_Code is not available!", System.Text.Encoding.UTF8, "application/json")
                };
            }
            // Build oject
            Build build = new Build
            {
                BuildID = buildDetails.BuildID,
                MaterialId = buildDetails.MaterialId,
                Status = buildDetails.Status,
                QR_code = buildDetails.QR_code
            };
            // Magic oject
            List<Magic> magicList = new List<Magic>();
            foreach (var item in buildDetails.Magic)
            {
                Magic tempMagic = new Magic
                {
                    BuildId = item.BuildId,
                    FileName = item.FileName,
                    MagicScreenshot = item.MagicScreenshot,
                    MagicID = item.MagicID
                };
                magicList.Add(tempMagic);
            }
            build.Magic = magicList;
            // SLM   oject
            List<SLM> slmList = new List<SLM>();
            foreach (var item in buildDetails.SLM)
            {
                SLM tempSlm = new SLM
                {
                    BuildId = item.BuildId,
                    FileName = item.FileName,
                    SLMID = item.SLMID
                };
                slmList.Add(tempSlm);
            }
            build.SLM = slmList;
            // GomBuild object
            List<GomBuild> gombuildList = new List<GomBuild>();
            foreach (var item in buildDetails.GomBuild)
            {
                GomBuild tempGombuild = new GomBuild
                {
                    BuildId = item.BuildId,
                    FileName = item.FileName,
                    GomBuildID = item.GomBuildID
                };
                gombuildList.Add(tempGombuild);
            }
            build.GomBuild = gombuildList;

            // Part + Gompart  object
            List<Part> partList = new List<Part>();
            foreach (var item in buildDetails.Part)
            {
                Part tempPart = new Part
                {
                    PartID = item.PartID,
                    StlFileName = item.StlFileName,
                    PrtFileName = item.PrtFileName,
                    QR_code = item.QR_code,
                    BuildId = item.BuildId,
                    MagicID = item.MagicID
                };

                // For each part list of of gom files
                List<GomPart> gompartList = new List<GomPart>();
                foreach (var item1 in item.GomPart)
                {
                    GomPart tempGomPart = new GomPart
                    {
                        PartId = item1.PartId,
                        GomPartID = item1.GomPartID,
                        FileName = item1.FileName
                    };
                    gompartList.Add(tempGomPart);
                }
                tempPart.GomPart = gompartList;
                partList.Add(tempPart);
            }
            build.Part = partList;
            // Converting Object to JSON string and cancel the null values
            string json = JsonConvert.SerializeObject(build, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        private bool IsInteger(int status)
        {
            if (status == (int)status) return true;
            else return false;
        }

        [HttpPut]
        [Route("UpdateBuild")]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult UpdateBuild(String qrcode, dynamic obj)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (obj.build == null)
            {
                return BadRequest("Invalid QR_code!");
            }

            Build build = new Build();
            try
            {
                string QR_code = Convert.ToString(qrcode);
                build = db.Build.FirstOrDefault(x => x.QR_code == qrcode);

                if (build == null)
                {
                    return BadRequest("Couldn't find build!");
                }

                if (obj.build.materialId != null)
                {
                    build.MaterialId = obj.build.materialId;
                }

                if (obj.build.status != null)
                {
                    build.Status = obj.build.status;
                }

                if (obj.build.magic != null)
                {

                    Magic dbMagic = db.Magic.FirstOrDefault(x => x.BuildId == build.BuildID);

                    if (dbMagic == null)
                    {
                        return BadRequest("Couldn't fetch the stored magic object from db to uppdate. Is the buildId given correct?");
                    }

                    Magic magic = BuildValidation.parseMagic(obj);
                    if (!BuildValidation.isFileNameNull(magic.FileName))
                    {
                        if (BuildValidation.isCorrectFileExtension(magic.FileName, "magic"))
                        {
                            dbMagic.FileName = magic.FileName;
                        }
                        else
                        {
                            return BadRequest("Updated magic file with incorrect extension!");
                        }
                    }
                    if (!BuildValidation.isFileNameNull(magic.MagicScreenshot))
                    {
                        if (BuildValidation.isCorrectFileExtension(magic.MagicScreenshot, "magicScreenshot"))
                        {
                            dbMagic.MagicScreenshot = magic.MagicScreenshot;
                        }
                        else
                        {
                            return BadRequest("Updated magic screenshot with incorrect extension!");
                        }
                    }
                }

                if (obj.build.slm != null)
                {
                    SLM dbSLM = db.SLM.FirstOrDefault(x => x.BuildId == build.BuildID);

                    if (dbSLM == null)
                    {
                        return BadRequest("Couldn't fetch the stored SLM object from db to uppdate. Is the buildId given correct?");
                    }

                    SLM slm = BuildValidation.parseSlm(obj);
                    if (!BuildValidation.isFileNameNull(slm.FileName))
                    {
                        if (BuildValidation.isCorrectFileExtension(slm.FileName, "slm"))
                        {
                            dbSLM.FileName = slm.FileName;
                        }
                        else
                        {
                            return BadRequest("The uploaded slm file had incorrect extension!");
                        }
                    }
                }

                if (obj.build.gombuilds != null)
                {
                    foreach (var item in obj.build.gombuilds)
                    {
                        //Each gombuild has to have its it included in the uploading
                        //Else we cannot map the changes to the right record in the db  
                        try
                        {
                            int gomid = item.GomBuildID;
                            if (gomid == 0)
                            {
                                return BadRequest("Unable to read id for gombuild!");
                            }

                            GomBuild dbGomBuild = db.GomBuild.FirstOrDefault(x => x.GomBuildID == gomid && x.BuildId == build.BuildID);
                            if (dbGomBuild == null)
                            {
                                return BadRequest("Unable to fetch gom build from db. Are you sure you have entered the right GomBuildID?");
                            }
                            else
                            {
                                if (!BuildValidation.isFileNameNull(item.FileName))
                                {
                                    if (BuildValidation.isCorrectFileExtension(item.FileName, "gombuild"))
                                    {
                                        string filename = Convert.ToString(item.FileName);
                                        if (dbGomBuild.FileName != filename)
                                        {
                                            dbGomBuild.FileName = item.FileName;
                                        }
                                    }
                                    else
                                    {
                                        return BadRequest("One of the uploaded gom files had incorrect file extension!");
                                    }
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            throw e;
                        }
                    }
                }

                db.SaveChanges();
            }

            catch (DbUpdateConcurrencyException error)
            {
                throw error;
            }
            catch (Exception e)
            {
                throw e;
            }

            return Ok("Build updated successfully!");
        }

        // POST: api/Build
        [HttpPost]
        [Route("CreateBuild")]
        [ResponseType(typeof(Build))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateBuild([FromBody] dynamic obj)
        {
            Build build = new Build();
            if ((obj.build.materialId == null))
                return BadRequest(ModelState);
            else
            {
                Dictionary<string, List<string>> mandatoryFiles = BuildValidation.getMandatoryFiles(obj);

                //MANDATORY FILES: STL, MACIGS, MACIGSCREENSHOT
                if (BuildValidation.allMandatoryFiles(mandatoryFiles))
                {
                    if (!BuildValidation.hasMagicsIDs(obj))
                    {
                        return BadRequest("Some parts didn't have any magicsIDs specified!");
                    }

                    string magics = BuildValidation.getMagic(mandatoryFiles).First();
                    string magicScreenShots = BuildValidation.getMagicScreenShot(mandatoryFiles).First();
                    List<string> stls = BuildValidation.getStls(mandatoryFiles);

                    if (BuildValidation.isCorrectFileExtensionsMandatory(mandatoryFiles))
                    {
                        try
                        {
                            build.MaterialId = obj.build.materialId;
                            build.Status = 0;
                            db.Build.Add(build);
                            db.SaveChanges();
                            // ## PART1 UPDATE THE QRCODE - START ##//
                            build.QR_code = "B" + build.BuildID;

                            db.SaveChanges();
                            PrintingInfo printingInfo = new PrintingInfo()
                            {
                                BuildStatus = "-",
                                Comments = "-",
                                DPCFactor = 0,
                                StartTime = DateTime.Today,
                                EndTime = DateTime.Today,
                                PrintingDate = DateTime.Today,
                                Operator = "-",
                                TypeOfmachine = "-",
                                PowderWeightStart = 0.0,
                                PowderweightEnd = 0.0,
                                WeightPowderWaste = 0.0,
                                PowerUsed = 0.0,
                                PlatformMaterial = "-",
                                PlatformWeight = 0.0,
                                PrintedTime = "-",
                                PowderCondition = "-",
                                NumberLayers = 0,
                                MinExposureTime = "-",
                                BuildId = build.BuildID
                            };
                            db.PrintingInfo.Add(printingInfo);
                            /*VariableParameters parameters = new VariableParameters()
                            {
                                MaterialType = "steel",
                                Volume = 1.0,
                                Weight = (double)obj.build.weight,
                                ZHeight = (double)obj.build.zheight,
                                NumberOfLayers = (int)obj.build.numLayers,
                                UnitOfLength = "Centimeter",
                                InfillPercentage = 1.0,
                                FileName = obj.build.fileName,
                                BuildId = build.BuildID
                            };
                            db.VariableParameters.Add(parameters);*/
                            
                            Magic magic = BuildValidation.parseMagic(obj);
                            if ((magic.FileName == "" || magic.FileName == null) && (magic.MagicScreenshot == "" || magic.MagicScreenshot == null))
                            {
                                return BadRequest("Unable to retrieve filename for magic file or magic screenshot file!");
                            }
                            else
                            {
                                if (BuildValidation.isCorrectFileExtension(magic.FileName, "magic") &&
                                    BuildValidation.isCorrectFileExtension(magic.MagicScreenshot, "magicScreenshot"))
                                {
                                    magic.BuildId = build.BuildID;
                                    db.Magic.Add(magic);
                                }
                                else
                                {
                                    return BadRequest("Uploaded magic file or magic screenshot with incorrect extension!");
                                }

                            }
                            SLM slm = BuildValidation.parseSlm(obj);
                            if (slm.FileName == null)
                            {
                                return BadRequest("No correct filename or unable to retrieve buildid for slm!");
                            }
                            else
                            {
                                if (BuildValidation.isCorrectFileExtension(slm.FileName, "slm"))
                                {
                                    slm.BuildId = build.BuildID;
                                    db.SLM.Add(slm);
                                }
                            }
                            foreach (var item in obj.build.gombuilds)
                            {
                                if (BuildValidation.isCorrectFileExtension(item, "gombuild"))
                                {
                                    GomBuild gombuild = new GomBuild();
                                    gombuild.BuildId = build.BuildID;
                                    gombuild.FileName = item;
                                    db.GomBuild.Add(gombuild);
                                }
                            }
                            foreach (var item in obj.build.parts)
                            {

                                foreach (var magicID in item.magicsIDs)
                                {
                                    Part part = new Part();
                                    part.BuildId = build.BuildID;

                                    if (BuildValidation.isCorrectFileExtension(item["stl"], "stl") &&
                                        BuildValidation.isCorrectFileExtension(item["prt"], "prt"))
                                    {
                                        part.StlFileName = item["stl"];
                                        part.PrtFileName = item["prt"];
                                    }
                                    else
                                    {
                                        return BadRequest("Stl or prt file was in wrong file extension!");
                                    }
                                    part.MagicID = magicID;

                                    db.Part.Add(part);
                                    db.SaveChanges();    

                                    // ## PART UPDATE THE QRCODE - START ##//
                                    var partTemp = db.Part.Where(p => p.PartID == part.PartID).FirstOrDefault<Part>();
                                    partTemp.QR_code = "P" + part.PartID;
                                    // Mark entity as modified
                                    db.Entry(part).State = System.Data.Entity.EntityState.Modified;
                                    db.SaveChanges();
                                    //Create General Test for each part
                                    var checkNoGeneralTest = db.GeneralTest.FirstOrDefault(x => x.PartId == part.PartID);
                                    if (checkNoGeneralTest != null)
                                    {
                                        return BadRequest("There already exist one General Test for this part. Please update it instead of creating a new!");
                                    }

                                    GeneralTest generalTest = new GeneralTest()
                                    {
                                        SupportRemoval = false,
                                        WEDM = false,
                                        WEDMComments = "-",
                                        Blasting = false,
                                        BlastingComments = "-",
                                        PartId = part.PartID,
                                        QRCode = part.QR_code
                                    };
                                    db.GeneralTest.Add(generalTest);
                                    db.SaveChanges();
                                    // GOM PART; THERE ARE MUTIPLE GOMPART FOR EACH PART
                                    foreach (var gomitem in item["gompart"])
                                    {
                                        if (BuildValidation.isCorrectFileExtension(Convert.ToString(gomitem), "gompart"))
                                        {
                                            GomPart gompart = new GomPart();
                                            gompart.PartId = part.PartID;
                                            gompart.FileName = gomitem;
                                            db.GomPart.Add(gompart);
                                        }
                                    }
                                }
                            }
                            db.SaveChanges();
                        }
                        catch (Exception e)
                        {
                            throw e;
                        }
                    }
                    else
                    {
                        return BadRequest("Not all files had the correct file extension!");
                    }
                }
                else
                {
                    return BadRequest("Not all mandatory files, or incorrect number of allowed files, uploaded!");
                }
            }
            CostEstimation estimation = new CostEstimation();
            ConstantParameters constantParameters = db.ConstantParameters.FirstOrDefault(x => x.ConstID == 1);
            estimation = CostEstimater.Cost((int)build.BuildID, (string)obj.build.fileName);
            estimation.CostEstimate = estimation.CostEstimate + Convert.ToInt32(600 * (constantParameters.BuildRemovalTime + constantParameters.PartRemovalTime +
                constantParameters.SetupTime +
                constantParameters.JobStartTime +
                constantParameters.PowderHandlingTime + constantParameters.MachineCleaningTime));
            estimation.PrintingTime = estimation.PrintingTime / 60;
            EstimatedValues estimated = new EstimatedValues
            {
                TotalPowderUsed = estimation.TotalPowderUsed,
                TotalPrintingTime = estimation.PrintingTime,
                TotalCost = estimation.CostEstimate,
                BuildId = build.BuildID
            };
            db.EstimatedValues.Add(estimated);
            db.SaveChanges();
            return Ok(build.BuildID);
        }

        // DELETE: api/Build/5
        [Route("DeleteBuild")]
        [ResponseType(typeof(Build))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteBuild(int id)
        {
            Build build = db.Build.Find(id);
            if (build == null)
            {
                return NotFound();
            }

            db.Build.Remove(build);
            db.SaveChanges();

            return Ok(build);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool BuildExists(int id)
        {
            return db.Build.Count(e => e.BuildID == id) > 0;
        }
    }
}