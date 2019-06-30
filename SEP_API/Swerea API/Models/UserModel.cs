using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Swerea_API
{
    public class UserModel
    {
        [Required]
        [Display(Name = "Username")]
        public string Username { get; set; }

        [Required]
        [Display(Name = "Firstname")]
        public string FirstName { get; set; }

        [Required]
        [Display(Name = "Lastname")]
        public string LastName { get; set; }

        [Required]
        [RegularExpression("(-?[1-9]+)", ErrorMessage = "Authorization level cannot be 0.")]
        [Display(Name = "Level")]
        public int Level { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "Password")]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "ConfirmPassword")]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }
    }
}