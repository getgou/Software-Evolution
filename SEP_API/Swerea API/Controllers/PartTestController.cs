using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using Swerea_API.Models;
using Newtonsoft.Json;
using Swerea_API.Models.Swerea_API.Models;
using Swerea_API.utils;

namespace Swerea_API.Controllers
{
    [RoutePrefix("PartTest")]
    public class PartTestController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/PartTest                         
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<PartTest> GetPartTest()
        {
            return db.PartTest;
        }

        [HttpGet]
        [Route("GetNumberOfTests")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetNumberOfTests(string qrcode)
        {
            if (qrcode == null || qrcode == "")
            {
                return BadRequest("Couldn't read qrcode!");
            }

            Part part = db.Part.FirstOrDefault(x => x.QR_code == qrcode);

            if (part == null)
            {
                return BadRequest("Couldn't find part with the given qrcode!");
            }

            var numGenTest = getNumGeneralTests(part.PartID);
            var numStressTest = getNumStressTests(part.PartID);
            var numHardeningTests = getNumHardeningTests(part.PartID);
            var numTamperingTest = getNumTamperingTests(part.PartID);
            var numAgingTest = getNumAgingTests(part.PartID);
            var numSolTreatTest = getNumSolutionTreatmentTests(part.PartID);

            NumberTestsModel numberTests = new NumberTestsModel()
            {
                GeneralTests = numGenTest,
                StressRelievingTests = numStressTest,
                HardeningTests = numHardeningTests,
                TamperingTests = numTamperingTest,
                AgingTests = numAgingTest,
                SolutionTreatmenTest = numSolTreatTest
            };

            return Ok(numberTests);

        }

        private int getNumGeneralTests(int partId)
        {
            return db.GeneralTest.Where(x => x.PartId == partId).Count();
        }

        private int getNumStressTests(int partId)
        {
            var partDetails = db.PartTestDetails.Where(x => x.PartId == partId).ToList();

            List<PartTest> stressTests = new List<PartTest>();

            foreach (var partDetail in partDetails)
            {
                PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partDetail.PartTestId && x.Type == 0);

                if (partTest != null)
                {
                    stressTests.Add(partTest);
                }
            }
            return stressTests.Count();
        }

        private int getNumHardeningTests(int partId)
        {
            var partDetails = db.PartTestDetails.Where(x => x.PartId == partId).ToList();

            List<PartTest> hardeningTests = new List<PartTest>();

            foreach (var partDetail in partDetails)
            {
                PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partDetail.PartTestId && x.Type == 1);

                if (partTest != null)
                {
                    hardeningTests.Add(partTest);
                }
            }
            return hardeningTests.Count();
        }

        private int getNumTamperingTests(int partId)
        {
            var partDetails = db.PartTestDetails.Where(x => x.PartId == partId).ToList();

            List<PartTest> tamperingTests = new List<PartTest>();

            foreach (var partDetail in partDetails)
            {
                PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partDetail.PartTestId && x.Type == 2);

                if (partTest != null)
                {
                    tamperingTests.Add(partTest);
                }
            }
            return tamperingTests.Count();
        }

        private int getNumSolutionTreatmentTests(int partId)
        {
            var partDetails = db.PartTestDetails.Where(x => x.PartId == partId).ToList();

            List<PartTest> solTreatTests = new List<PartTest>();

            foreach (var partDetail in partDetails)
            {
                PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partDetail.PartTestId && x.Type == 3);

                if (partTest != null)
                {
                    solTreatTests.Add(partTest);
                }
            }
            return solTreatTests.Count();
        }

        private int getNumAgingTests(int partId)
        {
            var partDetails = db.PartTestDetails.Where(x => x.PartId == partId).ToList();

            List<PartTest> agingTests = new List<PartTest>();

            foreach (var partDetail in partDetails)
            {
                PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partDetail.PartTestId && x.Type == 4);

                if (partTest != null)
                {
                    agingTests.Add(partTest);
                }
            }
            return agingTests.Count();
        }

        // GET: api/PartTest/GetPartTest?qrcode=1
        [HttpGet]
        [Route("GetPartTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetPartTests(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .ToList();

            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();


            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }

            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        [HttpGet]
        [Route("GetGeneralTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetGeneralTest(String qrcode)
        {
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "text/plain"),
                    StatusCode = HttpStatusCode.BadRequest
                };
            }

            var generalTest = (from p in db.Part
                               join gt in db.GeneralTest on p.PartID equals gt.PartId
                               where p.PartID == part.PartID
                               select new
                               {
                                   gt.SupportRemoval,
                                   gt.WEDM,
                                   gt.WEDMComments,
                                   gt.Blasting,
                                   gt.BlastingComments,
                                   gt.GeneralID
                               });

            if (generalTest.Count() < 1)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Couldn't find any General tests for the specified part!", System.Text.Encoding.UTF8, "text/plain"),
                    StatusCode = HttpStatusCode.BadRequest
                };
            }

            var tempGen = generalTest.First();
            ReturnedGeneralTest retgeneralTest = new ReturnedGeneralTest()
            {
                SupportRemoval = tempGen.SupportRemoval,
                WEDM = tempGen.WEDM,
                wedmComment = tempGen.WEDMComments,
                Blasting = tempGen.Blasting,
                BlastingComments = tempGen.BlastingComments,
                GeneralId = tempGen.GeneralID
            };

            string json = JsonConvert.SerializeObject(retgeneralTest, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json"),
                StatusCode = HttpStatusCode.OK
            };
        }
        // GET: api/PartTest/GetStressTest?qrcode=1 & Type = 0
        [HttpGet]
        [Route("GetStressTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetStressTest(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .Where(x => x.Type == 0)
                           .ToList();
            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();

            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }

            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // GET: api/PartTest/GetHardeningTest?qrcode=1  
        // Type = 1
        [HttpGet]
        [Route("GetHardeningTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetHardeningTest(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .Where(x => x.Type == 1)
                           .ToList();
            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();

            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }

            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // GET: api/PartTest/GetTamperingTest?qrcode=1  
        // Type = 2
        [HttpGet]
        [Route("GetTamperingTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetTamperingTest(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .Where(x => x.Type == 2)
                           .ToList();
            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();

            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }

            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // GET: api/PartTest/GetSolutionTest?qrcode=1 
        // Type = 3
        [HttpGet]
        [Route("GetSolutionTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetSolutionTest(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .Where(x => x.Type == 3)
                           .ToList();
            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();

            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }
            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // GET: api/PartTest/GetAgingTest?qrcode=1 
        // Type = 4
        [HttpGet]
        [Route("GetAgingTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetAgingTest(String qrcode)
        {
            //## STEP 1:RETRIEVE PART ID BY QR CODE ##//
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qrcode)
                .FirstOrDefault();
            if (part == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            //## STEP 2:RETRIEVE PARTDETAILS ID BY PART ID ##//
            var res = (from p in db.Part
                       join pd in db.PartTestDetails on p.PartID equals pd.PartId
                       where p.PartID == part.PartID
                       select new
                       {
                           pd.PartTestId
                       }).ToList();

            var resList = (from pd in db.PartTestDetails
                           join pt in db.PartTest on pd.PartTestId equals pt.PartTestID
                           select new
                           {
                               pt.PartTestID,
                               pt.Temperature,
                               pt.TimeEvent,
                               pt.Comment,
                               pt.Name,
                               pt.Type
                           })
                           .Where(x => x.Type == 4)
                           .ToList();
            var stressList = (from pt in db.PartTest
                              join str in db.StressRelieveingTest on pt.PartTestID equals str.PartTestId
                              where pt.PartTestID == str.PartTestId
                              select new
                              {
                                  str.ShieldingGas,
                                  str.PartTestId,
                                  str.StressRelieveingTestID
                              }).ToList();

            //## STEP 3:CHECK TYPE =>0 THAT'S MEAN THERE IS STRESS ROW ##//
            List<ReturnedPartTest> partTestList = new List<ReturnedPartTest>();
            foreach (var item in resList)
            {
                bool existed = false;
                foreach (var itemRes in res)
                {
                    if (itemRes.PartTestId == item.PartTestID) existed = true;
                }
                if (existed)
                {
                    ReturnedPartTest partTest = new ReturnedPartTest
                    {
                        //ShieldingGas = null,   
                        PartTestId = item.PartTestID,
                        Temperature = item.Temperature,
                        TimeEvent = item.TimeEvent,
                        Name = item.Name,
                        Comment = item.Comment,
                        Type = item.Type
                    };

                    if (item.Type == 0)
                    {
                        partTest.ShieldingGas = stressList.Find(x => x.PartTestId == item.PartTestID).ShieldingGas;
                    }
                    partTestList.Add(partTest);
                }
            }

            string json = JsonConvert.SerializeObject(partTestList, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // PUT: api/PartTest/UpdatePartTest  
        [HttpPut]
        [Route("UpdatePartTest")]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult UpdatePartTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.PartTestId == null)
            {
                return BadRequest(ModelState);
            }
            // Get Part Test Details 
            PartTest partTest = db.PartTest.Find((int)obj.PartTestId);
            try
            {
                if (obj.temperature != null)
                    partTest.Temperature = obj.temperature;
                // 
                if (obj.timeevent != null)
                    partTest.TimeEvent = obj.timeevent;
                //
                if (obj.name != null)
                    partTest.Name = obj.name;
                //
                if (obj.comment != null)
                    partTest.Comment = obj.comment;
                //
                if (obj.type != null)
                    partTest.Type = obj.type;
                db.SaveChanges();
                if ((partTest.Type == 0) && (obj.shieldinggas != null))
                {
                    var stress = db.StressRelieveingTest.Select(str => new { str.StressRelieveingTestID, str.PartTestId })
                        .Where(x => x.PartTestId == partTest.PartTestID)
                        .First();
                    StressRelieveingTest stressTemp = db.StressRelieveingTest.Find(stress.StressRelieveingTestID);
                    stressTemp.ShieldingGas = obj.shieldinggas;
                    db.SaveChanges();
                }

            }
            catch (DbUpdateConcurrencyException)
            {
                throw;
            }
            return Ok("Success");
        }

        [HttpPut]
        [Route("UpdateGeneralTest")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult UpdateGeneralTest([FromBody] dynamic obj)
        {

            try
            {
                if (obj == null)
                {
                    return BadRequest("Could not read uploaded General Test");
                }
                if (obj.qrcode == null || obj.qrcode == "")
                {
                    return BadRequest("Could not read partId");
                }

                string qrcode = Convert.ToString(obj.qrcode);

                GeneralTest generalTest = db.GeneralTest.FirstOrDefault(x => x.QRCode == qrcode);


                if (generalTest == null)
                {
                    return BadRequest("Could not find General Test with the given part id!");
                }

                if (obj.supportRemoval != null)
                {
                    generalTest.SupportRemoval = obj.supportRemoval;
                }

                if (obj.wedm != null)
                {
                    generalTest.WEDM = obj.wedm;
                }

                if (obj.wedmComments != null)
                {
                    generalTest.WEDMComments = obj.wedmComments;
                }

                if (obj.blasting != null)
                {
                    generalTest.Blasting = obj.blasting;
                }

                if (obj.blastingComments != null)
                {
                    generalTest.BlastingComments = obj.blastingComments;
                }
            }

            catch (Exception e)
            {
                return BadRequest(e.InnerException.ToString());
            }

            db.SaveChanges();

            return Ok("Successfully updated the general test!");
        }

        // POST: api/PartTest/CreatePartTest
        [Route("CreatePartTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreatePartTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.PartTest.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.PartTest.qrcode;

            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            int partID = part.PartID;
            // ## CREATING PARTTEST ROWS & THE EXPECTED JSON PARAMETER IS LIST OF PART TESTS -START ##//
            foreach (var item in obj.PartTest.details)
            {
                // StressReliveing Test, Expected extra parameter which is ShieldingGas[bool]
                if (item.type != 0)
                {
                    // Create Part test 
                    PartTest tempPart = new PartTest();

                    if (item.temperature == null || item.temperature == "")
                    {
                        tempPart.Temperature = 0.0;
                    }
                    else
                    {
                        tempPart.Temperature = item.temperature;
                    }
                    if (item.timeevent == "" || item.timeevent == null)
                    {
                        tempPart.TimeEvent = DateTime.Today;
                    }
                    else
                    {
                        tempPart.TimeEvent = item.timeevent;
                    }

                    if (item.name == null || item.name == "")
                    {
                        tempPart.Name = "-";
                    }
                    else
                    {
                        tempPart.Name = item.Name;
                    }

                    if (item.Comment == null || item.Coment == "")
                    {
                        tempPart.Comment = "-";
                    }
                    else
                    {
                        tempPart.Comment = item.comment;
                    }

                    tempPart.Type = item.type;
                    db.PartTest.Add(tempPart);
                    db.SaveChanges();

                    PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                               x.Temperature == tempPart.Temperature &&
                                               x.TimeEvent == tempPart.TimeEvent &&
                                               x.Name == tempPart.Name &&
                                               x.Comment == tempPart.Comment &&
                                               x.Type == tempPart.Type);

                    // Create StressRelieveingTest Row
                    StressRelieveingTest stressTemp = new StressRelieveingTest
                    {
                        ShieldingGas = item.shieldinggas,
                        PartTestId = storedPartTest.PartTestID,
                    };
                    db.StressRelieveingTest.Add(stressTemp);
                    db.SaveChanges();
                    // Connecting the Part test with the part
                    PartTestDetails tempPartDetails = new PartTestDetails
                    {
                        PartId = partID,
                        PartTestId = storedPartTest.PartTestID
                    };
                    db.PartTestDetails.Add(tempPartDetails);
                }
                else
                {
                    return BadRequest("Cannot add Stress test in this method! ");
                }
            }
            db.SaveChanges();
            return Ok("The process has been done successfully");
        }

        // POST: api/PartTest/CreateAgingTest
        [Route("CreateAgingTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateAgingTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.qrcode;
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            if ((obj.temperature == null) ||
                (obj.timeevent == null) ||
                (obj.comment == null)) return BadRequest("All the fields are required, please checking the value of the type");
            int partID = part.PartID;
            int newPartTest = 0;
            try
            {
                // Create Part test  
                PartTest tempPart = new PartTest
                {
                    Temperature = obj.temperature,
                    TimeEvent = obj.timeevent,
                    Name = "",
                    Comment = obj.comment,
                    Type = 4
                };
                db.PartTest.Add(tempPart);
                db.SaveChanges();
                newPartTest = tempPart.PartTestID;
                // Create Part Test Details process 
                PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                           x.PartTestID == tempPart.PartTestID &&
                                           x.Temperature == tempPart.Temperature &&
                                           x.TimeEvent == tempPart.TimeEvent &&
                                           x.Comment == tempPart.Comment &&
                                           x.Type == tempPart.Type);
                // Connecting the Part test with the part by using PartTestDetails
                PartTestDetails tempPartDetails = new PartTestDetails
                {
                    PartId = partID,
                    PartTestId = storedPartTest.PartTestID
                };
                db.PartTestDetails.Add(tempPartDetails);
                db.SaveChanges();
            }
            catch (Exception e) { throw; }
            return Ok(newPartTest);
        }

        // POST: api/PartTest/CreateHardeningTest
        [Route("CreateHardeningTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateHardeningTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.qrcode;
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            if ((obj.temperature == null) ||
                (obj.timeevent == null) ||
                (obj.comment == null)) return BadRequest("All the fields are required, please checking the value of the type");
            int partID = part.PartID;
            int newPartTest = 0;
            try
            {
                // Create Part test  
                PartTest tempPart = new PartTest
                {
                    Temperature = obj.temperature,
                    TimeEvent = obj.timeevent,
                    Name = "",
                    Comment = obj.comment,
                    Type = 1
                };
                db.PartTest.Add(tempPart);
                db.SaveChanges();
                newPartTest = tempPart.PartTestID;
                // Create Part Test Details process 
                PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                            x.PartTestID == tempPart.PartTestID &&
                                           x.Temperature == tempPart.Temperature &&
                                           x.TimeEvent == tempPart.TimeEvent &&
                                           x.Comment == tempPart.Comment &&
                                           x.Type == tempPart.Type);
                // Connecting the Part test with the part by using PartTestDetails
                PartTestDetails tempPartDetails = new PartTestDetails
                {
                    PartId = partID,
                    PartTestId = storedPartTest.PartTestID
                };
                db.PartTestDetails.Add(tempPartDetails);
                db.SaveChanges();
            }
            catch (Exception e) { throw; }
            return Ok(newPartTest);
        }

        // POST: api/PartTest/CreateSolutionTest
        [Route("CreateSolutionTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateSolutionTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.qrcode;
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            if ((obj.temperature == null) ||
                (obj.timeevent == null) ||
                (obj.comment == null)) return BadRequest("All the fields are required, please checking the value of the type");
            int partID = part.PartID;
            int newPartTest = 0;
            try
            {
                // Create Part test  
                PartTest tempPart = new PartTest
                {
                    Temperature = obj.temperature,
                    TimeEvent = obj.timeevent,
                    Name = "",
                    Comment = obj.comment,
                    Type = 3
                };
                db.PartTest.Add(tempPart);
                db.SaveChanges();
                newPartTest = tempPart.PartTestID;
                // Create Part Test Details process 
                PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                           x.PartTestID == tempPart.PartTestID &&
                                           x.Temperature == tempPart.Temperature &&
                                           x.TimeEvent == tempPart.TimeEvent &&
                                           x.Comment == tempPart.Comment &&
                                           x.Type == tempPart.Type);
                // Connecting the Part test with the part by using PartTestDetails
                PartTestDetails tempPartDetails = new PartTestDetails
                {
                    PartId = partID,
                    PartTestId = storedPartTest.PartTestID
                };
                db.PartTestDetails.Add(tempPartDetails);
                db.SaveChanges();
            }
            catch (Exception e) { throw; }
            return Ok(newPartTest);
        }


        // POST: api/PartTest/CreateStressTest
        [Route("CreateStressTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateStressTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.qrcode;
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            if ((obj.temperature == null) ||
                (obj.timeevent == null) ||
                (obj.comment == null) ||
                (obj.shieldinggas == null)) return BadRequest("All the fields are required, please checking the value of the type");
            int partID = part.PartID;
            int newPartTest = 0;
            try
            {
                // Create Part test  
                PartTest tempPart = new PartTest
                {
                    Temperature = obj.temperature,
                    TimeEvent = obj.timeevent,
                    Comment = obj.comment,
                    Type = 0
                };
                db.PartTest.Add(tempPart);
                db.SaveChanges();
                newPartTest = tempPart.PartTestID;
                // Create Part Test Details process 
                PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                           x.PartTestID == tempPart.PartTestID &&
                                           x.Temperature == tempPart.Temperature &&
                                           x.TimeEvent == tempPart.TimeEvent &&
                                           x.Comment == tempPart.Comment &&
                                           x.Type == tempPart.Type);
                // Create StressRelieveingTest Row
                StressRelieveingTest stressTemp = new StressRelieveingTest
                {
                    ShieldingGas = obj.PartTest.details.shieldinggas,
                    PartTestId = storedPartTest.PartTestID,
                };
                db.StressRelieveingTest.Add(stressTemp);
                db.SaveChanges();
                // Connecting the Part test with the part by using PartTestDetails
                PartTestDetails tempPartDetails = new PartTestDetails
                {
                    PartId = partID,
                    PartTestId = storedPartTest.PartTestID
                };
                db.PartTestDetails.Add(tempPartDetails);
                db.SaveChanges();
            }
            catch (Exception e) { throw; }
            return Ok(newPartTest);
        }

        // POST: api/PartTest/CreateTamperingTest
        [Route("CreateTamperingTest")]
        [HttpPost]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateTamperingTest([FromBody] dynamic obj)
        {
            // Validation Layer
            if (obj.qrcode == null)
            {
                return BadRequest(ModelState);
            }
            // ## CATCHING THE QR CODE & RETRIEVE PART DETAILS START ##//
            string qr_code = obj.qrcode;
            var part = db.Part.Select(x => new { x.QR_code, x.PartID })
                .Where(x => x.QR_code == qr_code)
                .FirstOrDefault();
            if (part == null) return BadRequest();
            if ((obj.temperature == null) ||
                (obj.timeevent == null) ||    
                (obj.comment == null)) return BadRequest("All the fields are required, please checking the value of the type");
            int partID = part.PartID;
            int newPartTest = 0;
            try
            {
                // Create Part test  
                PartTest tempPart = new PartTest
                {
                    Temperature = obj.temperature,
                    TimeEvent = obj.timeevent,
                    Name = "",
                    Comment = obj.comment,
                    Type = 2
                };
                db.PartTest.Add(tempPart);
                db.SaveChanges();
                newPartTest = tempPart.PartTestID;
                // Create Part Test Details process 
                PartTest storedPartTest = db.PartTest.FirstOrDefault(x =>
                                           x.PartTestID == tempPart.PartTestID &&
                                           x.Temperature == tempPart.Temperature &&
                                           x.TimeEvent == tempPart.TimeEvent &&
                                           x.Comment == tempPart.Comment &&
                                           x.Type == tempPart.Type);
                // Connecting the Part test with the part by using PartTestDetails
                PartTestDetails tempPartDetails = new PartTestDetails
                {
                    PartId = partID,
                    PartTestId = storedPartTest.PartTestID
                };
                db.PartTestDetails.Add(tempPartDetails);
                db.SaveChanges();
            }
            catch (Exception e) { throw; }
            return Ok(newPartTest);
        }


        //TODO: Create one generic delete method!!!!
        // DELETE: api/PartTest/5                      
        [HttpDelete]
        [Route("DeletePartTest")]
        [ResponseType(typeof(PartTest))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeletePartTestOld(int id)
        {
            //try
            //{
            PartTest partTest = db.PartTest.Find(id);
            if (partTest == null)
            {
                return NotFound();
            }
            // #STEP 3: DELETE PART TEST DETAILS
            var pd = db.PartTestDetails.Select(p1 => new { p1.PartTestDetailsID, p1.PartTestId })
            .Where(x => x.PartTestId == id)
            .FirstOrDefault();
            //
            PartTestDetails pdetials = db.PartTestDetails.Find(pd.PartTestDetailsID);
            if (pdetials == null)
            {
                return NotFound();
            }
            db.PartTestDetails.Remove(pdetials);
            db.SaveChanges();
            // #STEP 1: DELETE STRESS RELIEVEING 
            if (partTest.Type == 0)
            {
                var stressTest = db.StressRelieveingTest.Select(p1 => new { p1.StressRelieveingTestID, p1.PartTestId })
               .Where(x => x.PartTestId == id)
               .FirstOrDefault();
                StressRelieveingTest stress = db.StressRelieveingTest.Find(stressTest.StressRelieveingTestID);
                if (stress == null)
                {
                    return NotFound();
                }
                db.StressRelieveingTest.Remove(stress);
                db.SaveChanges();
            }
            // #STEP 2: DELETE PART TEST
            db.PartTest.Remove(partTest);
            db.SaveChanges();
            return Ok(partTest);
            //}
            //catch (Exception e)
            //{
            //    throw e;
            //}
        }

        // api/PartTest/DeleteAgingTest?qrcode=  
        [HttpDelete]
        [Route("DeletePartTest")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeletePartTest(int partTestId)
        {
            if (partTestId == 0)
            {
                return BadRequest("Could not read part test id to delete part Test!");
            }

            PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partTestId);

            if (partTest == null)
            {
                return BadRequest("Could not find part test with the given part test id!");
            }

            PartTestDetails partTestDetails = db.PartTestDetails.FirstOrDefault(x => x.PartTestId == partTestId);

            if (partTestDetails == null)
            {
                return BadRequest("Could not find part test details with the given part test id!");
            }
            db.PartTest.Remove(partTest);
            db.PartTestDetails.Remove(partTestDetails);

            try
            {
                db.SaveChanges();
            }

            catch (Exception e)
            {
                return BadRequest("Oops something went wrong when trying delete part Test!");
            }

            return Ok("Successfully deleted Aging Test!");
        }

        // api/PartTest/DeleteStressTest?qrcode=
        [HttpDelete]
        [Route("DeleteStressTest")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteStressTest(int partTestId)
        {
            if (partTestId == 0)
            {
                return BadRequest("Could not read part test id to delete Stress Test!");
            }

            PartTest partTest = db.PartTest.FirstOrDefault(x => x.PartTestID == partTestId);

            if (partTest == null)
            {
                return BadRequest("Could not find part test with the given part test id!");
            }

            PartTestDetails partTestDetails = db.PartTestDetails.FirstOrDefault(x => x.PartTestId == partTestId);

            if (partTestDetails == null)
            {
                return BadRequest("Could not find part test details with the given part test id!");
            }

            StressRelieveingTest partStressTest = db.StressRelieveingTest.FirstOrDefault(x => x.PartTestId == partTestId);

            if (partStressTest == null)
            {
                return BadRequest("Could not find Stress Releaving Test with given part id!");
            }

            db.StressRelieveingTest.Remove(partStressTest);
            db.PartTestDetails.Remove(partTestDetails);
            db.PartTest.Remove(partTest);

            try
            {
                db.SaveChanges();
            }

            catch (Exception e)
            {
                return BadRequest("Oops something went wrong when trying delete Stress Test!");
            }

            return Ok("Successfully deleted Stress Test!");
        }

        [HttpDelete]
        [Route("DeleteGeneralTest")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteGeneralTest([FromUri] string qrcode)
        {
            if (qrcode == null || qrcode == "")
            {
                return BadRequest("Could not read qrcode to delete general test!");
            }

            var part = db.Part.FirstOrDefault(x => x.QR_code == qrcode);
            GeneralTest generalTestToDelete = db.GeneralTest.FirstOrDefault(x => x.PartId == part.PartID);


            if (generalTestToDelete == null)
            {
                return BadRequest("Could not find specified general test to delete!");
            }
            try
            {
                db.GeneralTest.Remove(generalTestToDelete);
                db.SaveChanges();
            }
            catch (Exception e)
            {
                return BadRequest("Oops something went wrong when trying delete generaltest!");
            }

            return Ok("Successfully deleted general test!");
        }
                                                    
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PartTestExists(int id)
        {
            return db.PartTest.Count(e => e.PartTestID == id) > 0;
        }
    }
}