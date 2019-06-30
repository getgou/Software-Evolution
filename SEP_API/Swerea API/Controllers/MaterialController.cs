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
    public class MaterialController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/Material    
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IQueryable<Material> GetMaterial()
        {
            return db.Material;
        }

        // GET: api/Material/5 && Using QR_CODE instead of ID    
        [ResponseType(typeof(Material))]  
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetMaterial(string code)
        {
            //Material material = db.Material.Find(id);
            var mat = db.Material.Where(x => x.QR_code == code).FirstOrDefault();
            if (mat == null)
                return BadRequest(ModelState);
            else
                return Ok(mat);
            //if (material == null)
            //{
            //    return NotFound();
            //}
            //return Ok(material);
        }

        // PUT: api/Material/5      
        [ResponseType(typeof(void))]         
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutMaterial(int id, Material material)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != material.MaterialID)
            {
                return BadRequest();
            }

            db.Entry(material).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!MaterialExists(id))
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

        // PUT: api/Material/5    
        [ResponseType(typeof(void))]   
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        private IHttpActionResult UpdateQRMaterial(int id, Material material)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != material.MaterialID)
            {
                return BadRequest();
            }

            db.Entry(material).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!MaterialExists(id))
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

        // POST: api/Material     
        [HttpPost]                  
        [ResponseType(typeof(Material))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult CreateMaterial()
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            Material material = new Material();
            db.Material.Add(material);
            db.SaveChanges();
            // ## UPDATE THE QRCODE - START ##//
            material.QR_code = "M" +material.MaterialID;
            UpdateQRMaterial(material.MaterialID, material);
            // ## UPDATE THE QRCODE - END ##//
            return CreatedAtRoute("DefaultApi", new { id = material.MaterialID }, material);
        }

        // DELETE: api/Material/5
        [Authorize]
        [ResponseType(typeof(Material))]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult DeleteMaterial(int id)
        {
            Material material = db.Material.Find(id);
            if (material == null)
            {
                return NotFound();
            }

            db.Material.Remove(material);
            db.SaveChanges();

            return Ok(material);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool MaterialExists(int id)
        {
            return db.Material.Count(e => e.MaterialID == id) > 0;
        }
    }
}