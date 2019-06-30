using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Swerea_API.Models.auth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;

namespace Swerea_API.Controllers
{
    [Authorize(Roles = "SuperAdmin, Admin")]
    [RoutePrefix("Roles")]
    public class RolesController : BaseApiController
    {
        [HttpGet]
        [Authorize(Roles = "SuperAdmin, Admin")]
        [Route("GetAllRoles")]
        public IHttpActionResult GetAllRoles()
        {                             
            var roleManager = this.AppRoleManager.Roles;
            var roles = this.AppRoleManager.Roles.ToList();

            return Ok(roles);
        }

        [HttpPost]        
        [Route("create")]
        [Authorize(Roles = "SuperAdmin, Admin")] 
        public async Task<IHttpActionResult> Create(RoleBindingModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var role = new IdentityRole { Name = model.Name };

            var result = await this.AppRoleManager.CreateAsync(role);

            if (!result.Succeeded)
            {
                return GetErrorResult(result);
            }

            Uri locationHeader = new Uri(Url.Link("GetRoleById", new { id = role.Id }));

            
            return Created(locationHeader, TheModelFactory.Create(role));

        }

        [Route("{id:guid}")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public async Task<IHttpActionResult> DeleteRole(string Id)
        {

            var role = await this.AppRoleManager.FindByIdAsync(Id);

            if (role != null)
            {
                IdentityResult result = await this.AppRoleManager.DeleteAsync(role);

                if (!result.Succeeded)
                {
                    return GetErrorResult(result);
                }

                return Ok();
            }

            return NotFound();

        }

        //Method to add or remove users from a role
        [Route("ManageUsersInRole")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public async Task<IHttpActionResult> ManageUsersInRole(UsersInRoleModel model)
        {
            var role = await this.AppRoleManager.FindByIdAsync(model.Id);

            if (role == null)
            {
                ModelState.AddModelError("", "Role does not exist");
                return BadRequest(ModelState);
            }

            //Add Users to role
            foreach (string user in model.EnrolledUsers)
            {
                var appUser = await this.AppUserManager.FindByIdAsync(user);

                if (appUser == null)
                {
                    ModelState.AddModelError("", String.Format("User: {0} does not exists", user));
                    continue;
                }

                if (!this.AppUserManager.IsInRole(user, role.Name))
                {
                    IdentityResult result = await this.AppUserManager.AddToRoleAsync(user, role.Name);

                    if (!result.Succeeded)
                    {
                        ModelState.AddModelError("", String.Format("User: {0} could not be added to role", user));
                    }
                    IdentityResult res = await this.AppUserManager.AddClaimAsync(appUser.Id, new System.Security.Claims.Claim("Role", role.Name));

                    if (!res.Succeeded)
                    {
                        ModelState.AddModelError("", String.Format("User: {0} could not be added to role", user));
                    }
                    var appUser2 = await this.AppUserManager.FindByIdAsync(user);
                }

            }

            //Delete user from role
            foreach (string user in model.RemovedUsers)
            {
                var appUser = await this.AppUserManager.FindByIdAsync(user);

                if (appUser == null)
                {
                    ModelState.AddModelError("", String.Format("User: {0} does not exists", user));
                    continue;
                }

                IdentityResult result = await this.AppUserManager.RemoveFromRoleAsync(user, role.Name);

                if (!result.Succeeded)
                {
                    ModelState.AddModelError("", String.Format("User: {0} could not be removed from role", user));
                }
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            return Ok();
        }
                                           
    }
}