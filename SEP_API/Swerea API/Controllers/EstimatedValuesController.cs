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
    [RoutePrefix("Parameter")]
    public class EstimatedValuesController : ApiController
    {
        private SEPDBEntities db = new SEPDBEntities();

        // GET: api/EstimatedValues
        public IQueryable<EstimatedValues> GetEstimatedValues()
        {
            return db.EstimatedValues;
        }

        // GET: api/EstimatedValues/5
        [HttpGet]
        [Route("GetVariableParameters")]
        [ResponseType(typeof(EstimatedValues))]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        public IHttpActionResult GetEstimatedValues(int id)
        {
            EstimatedValues estimatedValues = db.EstimatedValues.FirstOrDefault(x => x.BuildId == id);
            if (estimatedValues == null)
            {
                return NotFound();
            }

            return Ok(estimatedValues);
        }

        // PUT: api/EstimatedValues/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutEstimatedValues(int id, EstimatedValues estimatedValues)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != estimatedValues.ValueID)
            {
                return BadRequest();
            }

            db.Entry(estimatedValues).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EstimatedValuesExists(id))
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

        // POST: api/EstimatedValues
        [ResponseType(typeof(EstimatedValues))]
        public IHttpActionResult PostEstimatedValues(EstimatedValues estimatedValues)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.EstimatedValues.Add(estimatedValues);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = estimatedValues.ValueID }, estimatedValues);
        }

        // DELETE: api/EstimatedValues/5
        [ResponseType(typeof(EstimatedValues))]
        public EstimatedValues DeleteEstimatedValues(int id)
        {
            EstimatedValues estimatedValues = db.EstimatedValues.Find(id);
            if (estimatedValues == null)
            {
                return null;
            }

            db.EstimatedValues.Remove(estimatedValues);
            db.SaveChanges();

            return estimatedValues;
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool EstimatedValuesExists(int id)
        {
            return db.EstimatedValues.Count(e => e.ValueID == id) > 0;
        }
    }
}