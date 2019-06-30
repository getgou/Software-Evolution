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

namespace Swerea_API.Controllers
{
    public class GomPartController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/GomPart

        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<GomPart> GetGomPart()
        {
            return db.GomPart;
        }

        // GET: api/GomPart/5
        [ResponseType(typeof(GomPart))] 
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetGomPart(String qrcode)
        {
            var gom = db.GomPart
            .Where(x => x.Part.PartID == x.PartId && x.Part.QR_code == qrcode)
            .Select(x => new { x.FileName })
            .ToList();
            if (gom == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            string json = JsonConvert.SerializeObject(gom, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }

        // PUT: api/GomPart/5
        [ResponseType(typeof(void))]         
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutGomPart(int id, GomPart gomPart)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != gomPart.GomPartID)
            {
                return BadRequest();
            }

            db.Entry(gomPart).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GomPartExists(id))
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

        // POST: api/GomPart
        [HttpPost]
        [ResponseType(typeof(GomPart))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateGomPart(GomPart gomPart)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.GomPart.Add(gomPart);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = gomPart.GomPartID }, gomPart);
        }

        // DELETE: api/GomPart/5
        [ResponseType(typeof(GomPart))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteGomPart(int id)
        {
            GomPart gomPart = db.GomPart.Find(id);
            if (gomPart == null)
            {
                return NotFound();
            }

            db.GomPart.Remove(gomPart);
            db.SaveChanges();

            return Ok(gomPart);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool GomPartExists(int id)
        {
            return db.GomPart.Count(e => e.GomPartID == id) > 0;
        }
    }
}