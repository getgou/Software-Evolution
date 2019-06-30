using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;           
using System.Web.Http;
using System.Web.Http.Description;
using Swerea_API.Models;

namespace Swerea_API.Controllers
{
    [RoutePrefix("PrintingInfo")]
    public class PrintingInfoController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/PrintingInfo                     
        [Route("GetPrintingInfo")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]  
        public IQueryable<PrintingInfo> GetPrintingInfo()
        {
            return db.PrintingInfo;
        }

        // GET: api/PrintingInfo/5    
        [ResponseType(typeof(PrintingInfo))]
        [HttpGet]
        [Route("GetPrintingInfo")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetPrintingInfo(String qrcode)
        {
            int buildId = int.Parse(qrcode.Substring(1, qrcode.Length - 1));
            var build = db.Build.FirstOrDefault(x => x.BuildID == buildId);
            if (build == null)
            {
                return NotFound();
            }                                                   

            var printInforet =  new PrintingInfo();
            try
            {
                //var printInfo = db.PrintingInfo.Select(p => new { p.PrintingInfoID, p.BuildStatus, p.StartTime, p.EndTime, p.PrintingDate, p.Operator, p.TypeOfmachine, p.PowderWeightStart, p.PowderweightEnd, p.WeightPowderWaste, p.PowerUsed, p.PlatformMaterial, p.PlatformWeight, p.PrintedTime, p.PowderCondition, p.NumberLayers, p.DPCFactor, p.MinExposureTime, p.Comments, p.BuildId })
                var printInfo = db.PrintingInfo
                        .Where(p => p.BuildId == buildId)
                        .First();

                if (printInfo == null)
                {
                    return NotFound();
                }
                printInfo.Build = null;
                printInforet =  printInfo;
            }
            catch (InvalidOperationException e)
            {
                return NotFound();
            }
                
            return Ok(printInforet);
        }

        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        [ResponseType(typeof(PrintingInfo))]
        [Route("GetPrintingInfoByPartQRCode")]
        public IHttpActionResult GetPrintingInfoByPartQRCode(String qrcode)
        {
            int partId = int.Parse(qrcode.Substring(1, qrcode.Length - 1));
            var printInforet = new PrintingInfo();
            try
            {
                var part = db.Part.Select(p => new { p.BuildId, p.PartID })
              .Where(p => p.PartID == partId)
              .First();
                if (part != null)
                {
                    var printInfo = db.PrintingInfo
                           .Where(p => p.BuildId == part.BuildId)
                           .First();
                    if (printInfo == null)
                    {
                        return NotFound();
                    }                         
                    printInforet = new PrintingInfo
                    {
                        PrintingInfoID = printInfo.PrintingInfoID,
                        BuildStatus = printInfo.BuildStatus,
                        StartTime = printInfo.StartTime,  
                        EndTime = printInfo.EndTime,  
                        PrintingDate = printInfo.PrintingDate,
                        Operator = printInfo.Operator,
                        TypeOfmachine = printInfo.TypeOfmachine,
                        PowderWeightStart = printInfo.PowderWeightStart,
                        PowderweightEnd = printInfo.PowderweightEnd,
                        WeightPowderWaste = printInfo.WeightPowderWaste,
                        PowerUsed = printInfo.PowerUsed,   
                        PlatformMaterial = printInfo.PlatformMaterial,
                        PlatformWeight = printInfo.PlatformWeight,
                        PrintedTime = printInfo.PrintedTime,    
                        PowderCondition = printInfo.PowderCondition,
                        NumberLayers= printInfo.NumberLayers,    
                        DPCFactor = printInfo.DPCFactor,      
                        MinExposureTime = printInfo.MinExposureTime, 
                        Comments = printInfo.Comments,
                        BuildId = printInfo.BuildId   
                    };
                }
                else return NotFound();
            }

            catch (InvalidOperationException e)
            {
                return NotFound();
            }

            return Ok(printInforet);
        }                               

        // PUT: api/PrintingInfo/5 
        [HttpPut]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        [Route("UpdatePrintingInfo")]
        public IHttpActionResult UpdatePrintingInfo(String qrcode, PrintingInfo printingInfo)
        {
            int buildId;
            if (qrcode.Contains('p') || qrcode.Contains('P'))
            {
                int partId = int.Parse(qrcode.Substring(1, qrcode.Length - 1));
                var partTemp = db.Part.Select(x => new { x.BuildId, x.PartID })
                    .Where(x => x.PartID == partId)
                    .First();
                buildId = partTemp.BuildId;
            }
            else
            {
                buildId = int.Parse(qrcode.Substring(1, qrcode.Length - 1));
            }
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            if (printingInfo == null)
            {
                return BadRequest("Unable to read uploaded printing info!");
            }
            var printInfo = db.PrintingInfo.Select(x => new { x.BuildId, x.PrintingInfoID })
                .Where(x => x.BuildId == buildId)
                .First();
            if (buildId != printInfo.BuildId)
            {
                return BadRequest();
            }
            var result = db.PrintingInfo.SingleOrDefault(b => b.PrintingInfoID == printInfo.PrintingInfoID);
            if (result != null)
            {
                if (printingInfo.BuildStatus != null) result.BuildStatus = printingInfo.BuildStatus;
                if (printingInfo.StartTime != null) result.StartTime = printingInfo.StartTime;
                if (printingInfo.EndTime != null) result.EndTime = printingInfo.EndTime;
                if (printingInfo.PrintingDate != null) result.PrintingDate = printingInfo.PrintingDate;
                if (printingInfo.Operator != null) result.Operator = printingInfo.Operator;
                if (printingInfo.TypeOfmachine != null) result.TypeOfmachine = printingInfo.TypeOfmachine;
                if (printingInfo.PowderWeightStart != null) result.PowderWeightStart = printingInfo.PowderWeightStart;
                if (printingInfo.PowderweightEnd != null) result.PowderweightEnd = printingInfo.PowderweightEnd;
                if (printingInfo.WeightPowderWaste != null) result.WeightPowderWaste = printingInfo.WeightPowderWaste;
                if (printingInfo.PowerUsed != null) result.PowerUsed = printingInfo.PowerUsed;
                if (printingInfo.PlatformMaterial != null) result.PlatformMaterial = printingInfo.PlatformMaterial;
                if (printingInfo.PlatformWeight != null) result.PlatformWeight = printingInfo.PlatformWeight;
                if (printingInfo.PrintedTime != null) result.PrintedTime = printingInfo.PrintedTime;
                if (printingInfo.PowderCondition != null) result.PowderCondition = printingInfo.PowderCondition;
                if (printingInfo.NumberLayers != null) result.NumberLayers = printingInfo.NumberLayers;
                if (printingInfo.DPCFactor != null) result.DPCFactor = printingInfo.DPCFactor;
                if (printingInfo.MinExposureTime != null) result.MinExposureTime = printingInfo.MinExposureTime;
                if (result.Comments != null) result.Comments = printingInfo.Comments;
                try
                {
                    db.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }
            }
            db.Entry(printingInfo).State = EntityState.Modified;
            return StatusCode(HttpStatusCode.NoContent);
        }
                                                                  
        // POST: api/PrintingInfo            
        [HttpPost]
        [ResponseType(typeof(PrintingInfo))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        [Route("CreatePrintingInfo")]
        public IHttpActionResult CreatePrintingInfo([FromBody] PrintingInfo printingInfo)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.PrintingInfo.Add(printingInfo);
            db.SaveChanges();

            return Ok(printingInfo.BuildId);
        }

        // DELETE: api/PrintingInfo/5   
        [ResponseType(typeof(PrintingInfo))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeletePrintingInfo(int id)
        {
            PrintingInfo printingInfo = db.PrintingInfo.Find(id);
            if (printingInfo == null)
            {
                return NotFound();
            }

            db.PrintingInfo.Remove(printingInfo);
            db.SaveChanges();

            return Ok(printingInfo);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PrintingInfoExists(int id)
        {
            return db.PrintingInfo.Count(e => e.PrintingInfoID == id) > 0;
        }
    }
}