using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.ModelBinding;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using System.Linq;
using Swerea_API.Models;
using System.Web.Http.Description;
using System.Data.Entity;
using Swerea_API.Models.auth;

namespace Swerea_API.Controllers
{
    [RoutePrefix("Account")]
    public class AccountController : BaseApiController
    {
        [HttpGet]
        [Authorize(Roles = "SuperAdmin, Admin, Edit, Read")]
        [Route("GetInfoLoggedInUser")]
        public async Task<IHttpActionResult> GetInfoLoggedInUser()
        {

            var userId = ((ClaimsIdentity)User.Identity).Claims
                .Where(c => c.Type == ClaimTypes.NameIdentifier)
                    .Select(c => c.Value).ToList();
                           
            IdentityUser user = await this.AppUserManager.FindByIdAsync(Convert.ToString(userId.First()));
            
            var roleList = new List<IdentityRole>();                 
            foreach (var item in user.Roles)
            {
                var roleId = item.RoleId;

                var response = await this.AppRoleManager.FindByIdAsync(item.RoleId);
                var roleToReturn = new IdentityRole()
                {
                    Id = response.Id,
                    Name = response.Name        
                };
                roleList.Add(roleToReturn);
            }

            ReturnedUser userToReturn = new ReturnedUser()
            {
                id = user.Id,
                username = user.UserName,
                roles = roleList
            };

            return Ok(userToReturn);
        }

        [HttpGet]
        [Route("GetAllUsers")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult GetAllUsers()
        {
            return Ok(this.AppUserManager.Users.ToList());
        }

        //TODO: GetUserByUsername
        //Return: Roles the user are in.       

        [HttpGet]
        [Authorize]
        [Route("GetUserByUsername")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult GetUserByUsername(string roleId)
        {
            var users = this.AppUserManager.Users.Where(x => x.Roles.Select(y => y.RoleId).Contains(roleId)).ToList();
            return Ok(users);
        }

        [HttpGet]
        [Route("GetUsersInRole")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public IHttpActionResult GetUsersInRole(string roleId)
        {
            var users = this.AppUserManager.Users.Where(x => x.Roles.Select(y => y.RoleId).Contains(roleId)).ToList();
            return Ok(users);
        }

        // POST api/Account/Register 
        [Route("Register")]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public async Task<IHttpActionResult> Register(UserModel userModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            User user = new User()
            {
                UserName = userModel.Username,
                //Firstname = userModel.FirstName,
                //Lastname = userModel.LastName,
                Level = userModel.Level
            };

            //OWIN: Register user
            // IdentityResult result = await _authRepo.RegisterUser(userModel);
            IdentityResult result = await this.AppUserManager.CreateAsync(user, userModel.Password);

            if (!result.Succeeded)
            {
                return GetErrorResult(result);
            }
            var storedUser = this.AppUserManager.Users.FirstOrDefault(x => x.UserName == user.UserName);

            IdentityResult addRoleResult = new IdentityResult();
            //User registered as new SuperAdmin
            if (userModel.Level == -1)
            {
                addRoleResult = await this.AppUserManager.AddToRolesAsync(storedUser.Id, "SuperAdmin");
            }

            //User registered as Admin
            if (userModel.Level == 1)
            {
                addRoleResult = await this.AppUserManager.AddToRolesAsync(storedUser.Id, "Admin");
            }

            //User has Edit permissions
            if (userModel.Level == 2)
            {
                addRoleResult = await this.AppUserManager.AddToRolesAsync(storedUser.Id, "Edit");
            }

            //User has Read permissions
            if (userModel.Level == 3)
            {
                addRoleResult = await this.AppUserManager.AddToRolesAsync(storedUser.Id, "Read");
            }

            if (!addRoleResult.Succeeded)
            {
                ModelState.AddModelError("", "Failed to add user role");
                return BadRequest(ModelState);
            }

            return Ok("User registered Successfully!");
        }

        //[Authorize(Roles = "SuperAdmin")]   
        [HttpPut]
        [Authorize(Roles = "SuperAdmin, Admin")]
        public async Task<IHttpActionResult> AssignRolesToUser([FromUri] string id, [FromBody] string[] rolesToAssign)
        {

            var appUser = await this.AppUserManager.FindByIdAsync(id);

            if (appUser == null)
            {
                return NotFound();
            }

            var currentRoles = await this.AppUserManager.GetRolesAsync(appUser.Id);

            var rolesNotExists = rolesToAssign.Except(this.AppRoleManager.Roles.Select(x => x.Name)).ToArray();

            if (rolesNotExists.Count() > 0)
            {

                ModelState.AddModelError("", string.Format("Roles '{0}' does not exixts in the system", string.Join(",", rolesNotExists)));
                return BadRequest(ModelState);
            }

            IdentityResult removeResult = await this.AppUserManager.RemoveFromRolesAsync(appUser.Id, currentRoles.ToArray());

            if (!removeResult.Succeeded)
            {
                ModelState.AddModelError("", "Failed to remove user roles");
                return BadRequest(ModelState);
            }

            IdentityResult addResult = await this.AppUserManager.AddToRolesAsync(appUser.Id, rolesToAssign);

            if (!addResult.Succeeded)
            {
                ModelState.AddModelError("", "Failed to add user roles");
                return BadRequest(ModelState);
            }

            return Ok();
        }


    }
}
