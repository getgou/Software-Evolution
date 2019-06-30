using Microsoft.AspNet.Identity.EntityFramework;
using Swerea_API.Infrastructure;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Http.Routing;

namespace Swerea_API.Models.auth
{
    public class ModelFactory
    {
        private UrlHelper _UrlHelper;
        private ApplicationUserManager _AppUserManager;

        public ModelFactory(HttpRequestMessage request, ApplicationUserManager appUserManager)
        {
            _UrlHelper = new UrlHelper(request);
            _AppUserManager = appUserManager;
        }

        public RoleReturnModel Create(IdentityRole appRole)
        {

            return new RoleReturnModel
            {  
                Id = appRole.Id,
                Name = appRole.Name
            };
        }

        public UserReturnModel Create(User appUser)
        {
            return new UserReturnModel
            {
                Url = _UrlHelper.Link("GetUserById", new { id = appUser.Id }),
                Id = appUser.Id,
                UserName = appUser.UserName,
                //FullName = string.Format("{0} {1}", appUser.Firstname, appUser.Lastname),
                Level = appUser.Level,      
                Roles = _AppUserManager.GetRolesAsync(appUser.Id).Result,
                Claims = _AppUserManager.GetClaimsAsync(appUser.Id).Result
            };
        }
    }

    public class RoleReturnModel
    {                                       
        public string Id { get; set; }
        public string Name { get; set; }
    }

    public class UserReturnModel
    {
        public string Url { get; set; }
        public string Id { get; set; }
        public string UserName { get; set; }
        public string FullName { get; set; }     
        public int Level { get; set; }         
        public IList<string> Roles { get; set; }
        public IList<System.Security.Claims.Claim> Claims { get; set; }
    }
}