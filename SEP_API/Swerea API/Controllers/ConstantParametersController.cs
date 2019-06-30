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
    [RoutePrefix("Parameters")]
    public class ConstantParametersController : BaseApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/ConstantParameters
        public IQueryable<ConstantParameters> GetConstantParameters()
        {
            return db.ConstantParameters;
        }

        // GET: api/ConstantParameters/5
        [HttpGet]
        [Route("GetParameters")]
        [ResponseType(typeof(ConstantParameters))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetConstantParameters(int id)
        {
            ConstantParameters constantParameters = db.ConstantParameters.Find(id);
            if (constantParameters == null)
            {
                return NotFound();
            }

            return Ok(constantParameters);
        }

        // PUT: api/ConstantParameters/5
        [HttpPut]
        [Route("PutConstantParameters")]
        [ResponseType(typeof(void))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PutConstantParameters(int id, [FromBody] ConstantParameters constantParameters)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != constantParameters.ConstID)
            {
                return BadRequest("Invalid ID!");
            }

            db.Entry(constantParameters).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ConstantParametersExists(id))
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

        // POST: api/ConstantParameters
        [HttpPost]
        [Route("PostConstantParameters")]
        [ResponseType(typeof(ConstantParameters))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit")]
        public IHttpActionResult PostConstantParameters([FromBody] ConstantParameters constantParameters)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.ConstantParameters.Add(constantParameters);
            db.SaveChanges();

            return Ok(1);
        }

        // DELETE: api/ConstantParameters/5
        [ResponseType(typeof(ConstantParameters))]
        public IHttpActionResult DeleteConstantParameters(int id)
        {
            ConstantParameters constantParameters = db.ConstantParameters.Find(id);
            if (constantParameters == null)
            {
                return NotFound();
            }

            db.ConstantParameters.Remove(constantParameters);
            db.SaveChanges();

            return Ok(constantParameters);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ConstantParametersExists(int id)
        {
            return db.ConstantParameters.Count(e => e.ConstID == id) > 0;
        }
    }
}