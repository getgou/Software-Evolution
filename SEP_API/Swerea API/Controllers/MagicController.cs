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
    public class MagicController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/Magic
        public IQueryable<Magic> GetMagic()
        {
            return db.Magic;
        }

        // GET: api/Magic/5   
        [ResponseType(typeof(Magic))]   
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetMagic(int id)
        {
            Magic magic = db.Magic.Find(id);
            if (magic == null)
            {
                return NotFound();
            }

            return Ok(magic);
        }

        // PUT: api/Magic/5   
        [ResponseType(typeof(void))]        
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutMagic(int id, Magic magic)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != magic.MagicID)
            {
                return BadRequest();
            }

            db.Entry(magic).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!MagicExists(id))
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

        // POST: api/Magic   
        [HttpPost]
        [ResponseType(typeof(Magic))]   
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateMagic(Magic magic)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Magic.Add(magic);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = magic.MagicID }, magic);
        }

        // DELETE: api/Magic/5         
        [ResponseType(typeof(Magic))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteMagic(int id)
        {
            Magic magic = db.Magic.Find(id);
            if (magic == null)
            {
                return NotFound();
            }

            db.Magic.Remove(magic);
            db.SaveChanges();

            return Ok(magic);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool MagicExists(int id)
        {
            return db.Magic.Count(e => e.MagicID == id) > 0;
        }
    }
}