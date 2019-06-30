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
    [RoutePrefix("Part")]
    public class PartController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/Part          
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<Part> GetPart()
        {
            return db.Part;
        }

        // GET: api/Part/5 
        [ResponseType(typeof(Part))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public HttpResponseMessage GetPart(String qrcode)
        {
            var stls = db.Part
                .Where(x => x.QR_code == qrcode)
                .Select(x => new { x.StlFileName, x.PrtFileName })
                .ToList();
            if (stls == null)
            {
                return new HttpResponseMessage()
                {
                    Content = new StringContent("Error", System.Text.Encoding.UTF8, "application/json")
                };
            }
            string json = JsonConvert.SerializeObject(stls, new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            });
            return new HttpResponseMessage()
            {
                Content = new StringContent(json, System.Text.Encoding.UTF8, "application/json")
            };
        }
                             
        [HttpGet]            
        [Route("PartExist")]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult PartExist(string qrcode)
        {
            if(qrcode == null || qrcode == "")
            {
                return BadRequest(); 
            }

            try
            {
                var part = db.Part.FirstOrDefault(x => x.QR_code == qrcode);

                if(part == null)
                {
                    return Ok(false);
                }
                return Ok(true);
            }

            catch(InvalidOperationException e)
            {
                return Ok(false);
            }
        }
     
        // PUT: api/Part/5
        [Authorize]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutPart(int id, Part part)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != part.PartID)
            {
                return BadRequest();
            }

            db.Entry(part).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PartExists(id))
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

        // PUT: api/Part/5
        [Authorize]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        private IHttpActionResult UpdateQRPart(int id, Part part)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != part.PartID)
            {
                return BadRequest();
            }

            db.Entry(part).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PartExists(id))
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

        // POST: api/Part
        [Authorize]
        [ResponseType(typeof(Part))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult PostPart(Part part)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Part.Add(part);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = part.PartID }, part);
        }

        // DELETE: api/Part/5
        [Authorize]
        [ResponseType(typeof(Part))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeletePart(int id)
        {
            Part part = db.Part.Find(id);
            if (part == null)
            {
                return NotFound();
            }

            db.Part.Remove(part);
            db.SaveChanges();

            return Ok(part);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PartExists(int id)
        {
            return db.Part.Count(e => e.PartID == id) > 0;
        }
    }
}