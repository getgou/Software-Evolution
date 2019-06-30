using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OAuth;
using Swerea_API.Infrastructure;
using Swerea_API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;

namespace Swerea_API.Providers
{
    public class CustomOAuthProvider : OAuthAuthorizationServerProvider
    {

        public override Task ValidateClientAuthentication(OAuthValidateClientAuthenticationContext context)
        {   
            context.Validated();
            return Task.FromResult<object>(null);
        }

        public override async Task GrantResourceOwnerCredentials(OAuthGrantResourceOwnerCredentialsContext context)
        {

            //context.OwinContext.Response.Headers.Add("Access-Control-Allow-Origin", new[] { "*" });
           

            var userManager = context.OwinContext.GetUserManager<ApplicationUserManager>();

            User user = await userManager.FindAsync(context.UserName, context.Password);

            if (user == null)
            {
                context.SetError("invalid_grant", "The user name or password is incorrect.");
                return;
            }

            ClaimsIdentity oAthIdentity = await user.GenerateUserIdentityAsync(userManager, "JWT");

            var ticket = new AuthenticationTicket(oAthIdentity, null);          

            context.Validated(ticket);            
        }
    }
}