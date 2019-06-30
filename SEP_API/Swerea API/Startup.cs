using System;
using Microsoft.Owin;
using Owin;
using System.Web.Http;
using Microsoft.Owin.Security.OAuth;
using Swerea_API.Infrastructure;
using Swerea_API.Models;
using Swerea_API.Providers;
using System.Configuration;
using Microsoft.Owin.Security.DataHandler.Encoder;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Jwt;
using Newtonsoft.Json;
using Microsoft.Owin.Cors;

[assembly: OwinStartup(typeof(Swerea_API.Startup))]

namespace Swerea_API
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            HttpConfiguration config = new HttpConfiguration();

            ConfigureOAuthTokenGeneration(app);
            ConfigureOathTokenConsumption(app);
            config.Formatters.JsonFormatter.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore;
          

            WebApiConfig.Register(config);
            app.UseCors(Microsoft.Owin.Cors.CorsOptions.AllowAll);
            app.UseWebApi(config);
        }

        public void ConfigureOAuthTokenGeneration(IAppBuilder app)
        {
            app.CreatePerOwinContext(SEPEnteties.Create);
            app.CreatePerOwinContext<ApplicationUserManager>(ApplicationUserManager.Create);
            app.CreatePerOwinContext<ApplicationRoleManager>(ApplicationRoleManager.Create);
            app.UseCors(CorsOptions.AllowAll);

            OAuthAuthorizationServerOptions OAuthServerOptions = new OAuthAuthorizationServerOptions()
            {
                AllowInsecureHttp = true,
                TokenEndpointPath = new PathString("/token"),
                AccessTokenExpireTimeSpan = TimeSpan.FromDays(60),       
                Provider = new CustomOAuthProvider(),
                AccessTokenFormat = new TokenFormat("http://localhost58638")
            };

            // Token Generation
            app.UseOAuthAuthorizationServer(OAuthServerOptions);
            //app.UseOAuthBearerAuthentication(new OAuthBearerAuthenticationOptions());

        }


        public void ConfigureOathTokenConsumption(IAppBuilder app)
        {
            var issuer = "http://localhost58638";
            string audienceId = ConfigurationManager.AppSettings["as:AudienceId"];
            byte[] audienceSecret = TextEncodings.Base64Url.Decode(ConfigurationManager.AppSettings["as:AudienceSecret"]);

            // Api controllers with an [Authorize] attribute will be validated with JWT
            app.UseJwtBearerAuthentication(
                new JwtBearerAuthenticationOptions
                {
                    AuthenticationMode = AuthenticationMode.Active,
                    AllowedAudiences = new[] { audienceId },
                    IssuerSecurityTokenProviders = new IIssuerSecurityTokenProvider[]
                    {
                        new SymmetricKeyIssuerSecurityTokenProvider(issuer, audienceSecret)
                    }
                });

        }
    }
}
