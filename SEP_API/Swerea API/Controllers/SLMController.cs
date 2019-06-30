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

namespace Swerea_API.Controllers
{
    public class SLMController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/SLM               
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<SLM> GetSLM()
        {
            return db.SLM;
        }

        // GET: api/SLM/5
        [Authorize]
        [ResponseType(typeof(SLM))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetSLM(int id)
        {
            SLM sLM = db.SLM.Find(id);
            if (sLM == null)
            {
                return NotFound();
            }

            return Ok(sLM);
        }

        // PUT: api/SLM/5
        [Authorize]
        [ResponseType(typeof(void))]      
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutSLM(int id, SLM sLM)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != sLM.SLMID)
            {
                return BadRequest();
            }

            db.Entry(sLM).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!SLMExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/SLM           
        [HttpPost]
        [Authorize]
        [ResponseType(typeof(SLM))]   
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateSLM(SLM sLM)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.SLM.Add(sLM);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = sLM.SLMID }, sLM);
        }

        // DELETE: api/SLM/5
        [Authorize]
        [ResponseType(typeof(SLM))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteSLM(int id)
        {
            SLM sLM = db.SLM.Find(id);
            if (sLM == null)
            {
                return NotFound();
            }

            db.SLM.Remove(sLM);
            db.SaveChanges();

            return Ok(sLM);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool SLMExists(int id)
        {
            return db.SLM.Count(e => e.SLMID == id) > 0;
        }
    }
}