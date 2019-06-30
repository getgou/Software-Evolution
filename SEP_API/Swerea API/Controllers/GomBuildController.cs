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
    public class GomBuildController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/GomBuild          
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<GomBuild> GetGomBuild()
        {
            return db.GomBuild;
        }

        // GET: api/GomBuild/5
        [ResponseType(typeof(GomBuild))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetGomBuild(int id)
        {
            GomBuild gomBuild = db.GomBuild.Find(id);
            if (gomBuild == null)
            {
                return NotFound();
            }

            return Ok(gomBuild);
        }

        // PUT: api/GomBuild/5
        [Authorize]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutGomBuild(int id, GomBuild gomBuild)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != gomBuild.GomBuildID)
            {
                return BadRequest();
            }

            db.Entry(gomBuild).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GomBuildExists(id))
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

        // POST: api/GomBuild             
        [HttpPost] 
        [ResponseType(typeof(GomBuild))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateGomBuild(GomBuild gomBuild)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.GomBuild.Add(gomBuild);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = gomBuild.GomBuildID }, gomBuild);
        }

        // DELETE: api/GomBuild/5  
        [ResponseType(typeof(GomBuild))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteGomBuild(int id)
        {
            GomBuild gomBuild = db.GomBuild.Find(id);
            if (gomBuild == null)
            {
                return NotFound();
            }

            db.GomBuild.Remove(gomBuild);
            db.SaveChanges();

            return Ok(gomBuild);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool GomBuildExists(int id)
        {
            return db.GomBuild.Count(e => e.GomBuildID == id) > 0;
        }
    }
}