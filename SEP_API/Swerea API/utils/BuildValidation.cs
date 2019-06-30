using Newtonsoft.Json;
using Swerea_API.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace Swerea_API.utils
{
    public static class BuildValidation
    {

        public static Dictionary<string, List<string>> getMandatoryFiles(dynamic obj)
        {
            Dictionary<string, List<string>> dict = new Dictionary<string, List<string>>();

            List<string> magics = new List<string>();
            List<string> magicScreenshots = new List<string>();
            List<string> stls = new List<string>();

            if (obj.build.magic.filename != null)
            {
                magics.Add(Convert.ToString(obj.build.magic.filename));
            }
            if (obj.build.magic.magicscreenshot != null)
            {
                magicScreenshots.Add(Convert.ToString(obj.build.magic.magicscreenshot));
            }

            foreach (var item in obj.build.parts)
            {
                stls.Add(Convert.ToString(item["stl"]));
            }

            dict.Add("magics", magics);
            dict.Add("magicScreenshots", magicScreenshots);
            dict.Add("stls", stls);

            return dict;
        }

        public static bool allMandatoryFiles(Dictionary<string, List<string>> listOfFiles)
        {

            List<string> magic = getMagic(listOfFiles);
            List<string> magicScreenshot = getMagicScreenShot(listOfFiles);
            List<string> stl = getStls(listOfFiles);

            if (magic != null && magicScreenshot != null && stl != null)
            {
                if (magic.Count() == 1 && magicScreenshot.Count() == 1 && stl.Count() > 0)
                {
                    return true;
                }
            }

            return false;
        }

        public static List<string> getMagic(Dictionary<string, List<string>> listOfFiles)
        {
            if (listOfFiles.ContainsKey("magics"))
            {
                return listOfFiles["magics"]; ;
            }

            return null;
        }

        public static List<string> getMagicScreenShot(Dictionary<string, List<string>> listOfFiles)
        {
            if (listOfFiles.ContainsKey("magicScreenshots"))
            {
                return listOfFiles["magicScreenshots"]; ;
            }

            return null;
        }

        public static List<string> getStls(Dictionary<string, List<string>> listOfFiles)
        {
            if (listOfFiles.ContainsKey("stls"))
            {
                return listOfFiles["stls"]; ;
            }

            return null;
        }

        public static Magic parseMagic(dynamic obj)
        {
            Magic magic = new Magic();

            if (obj.build.magic != null)
            {
                magic.FileName = obj.build.magic.filename;
                magic.MagicScreenshot = obj.build.magic.magicscreenshot;
            }

            return magic;
        }

        public static SLM parseSlm(dynamic obj)
        {
            SLM slm = new SLM();

            if (obj.build.slm != null)
            {
                slm.FileName = obj.build.slm;
            }

            return slm;
        }

        public static bool isCorrectFileExtensionsMandatory(Dictionary<string, List<string>> listOfFiles)
        {
            bool checkedMagics = false;
            bool checkedMagicScreenShot = false;
            bool checkedStls = false;
            bool okMagics = true;
            bool okMagicScreenShot = true;
            bool okStls = true;

            if (listOfFiles.ContainsKey("magics"))
            {
                checkedMagics = true;
                foreach (var file in listOfFiles["magics"])
                {
                    if (!file.Contains(".magics"))
                    {
                        okMagics = false;
                    }
                }
            }

            if (listOfFiles.ContainsKey("magicScreenshots"))
            {
                checkedMagicScreenShot = true;
                foreach (var file in listOfFiles["magicScreenshots"])
                {
                    if (!file.Contains(".jpg") && !file.Contains(".jpeg") && !file.Contains(".png"))
                    {
                        okMagicScreenShot = false;
                    }
                }
            }

            if (listOfFiles.ContainsKey("stls"))
            {
                checkedStls = true;
                foreach (var file in listOfFiles["stls"])
                {
                    if (!file.Contains(".stl"))
                    {
                        okStls = false;
                    }
                }
            }

            if (okMagics && okMagicScreenShot && okStls && checkedMagics && checkedMagicScreenShot && checkedStls)
            {
                return true;
            }

            return false;
        }

        public static bool hasMagicsIDs(dynamic obj)
        {
            try
            {
                if(obj.build.parts != null)
                {
                    foreach(var part in obj.build.parts)
                    {
                        if(part.magicsIDs == null)
                        {
                            return false;
                        }

                        List<string> magicsIDs = new List<string>();
                        
                        foreach(var magicID in part.magicsIDs)
                        {
                            var tempMagic = Convert.ToString(magicID);
                            if (tempMagic != null && tempMagic != "")
                            {
                                magicsIDs.Add(tempMagic);     
                            }                   
                        }

                        if(!(magicsIDs.Count > 0))
                        {
                            return false;
                        }   
                    }
                    return true;
                }

                return false;
            }

            catch(Exception e)
            {
                return false;
            }
        }

        public static bool isFileNameNull(dynamic filename)
        {
            if (filename == "" || filename == null)
            {
                return true;
            }
            return false;      
        }

        public static bool isCorrectFileExtension(dynamic item, string type)
        {
            string extension = Path.GetExtension(Convert.ToString(item));

            switch (type)
            {
                case "gombuild":
                    if (extension.Contains(".goms"))
                    {
                        return false;
                    }
                    if (extension.Contains(".gom"))
                    {
                        return true;
                    }
                    return false;

                case "gompart":
                    if (extension.Contains(".goms"))
                    {
                        return false;
                    }
                    if (extension.Contains(".gom"))
                    {
                        return true;
                    }
                    return false;

                case "stl":
                    if (extension == ".stl")
                    {
                        return true;
                    }
                    return false;
                case "slm":
                    if (extension == ".slm")
                    {
                        return true;
                    }
                    return false;

                case "prt":
                    if (extension == ".prt")
                    {
                        return true;
                    }
                    return false;

                case "magic":
                    if (extension == ".magics")
                    {
                        return true;
                    }
                    return false;

                case "magicScreenshot":
                    if (extension == ".jpg" || extension == ".jpeg" || extension == ".png")
                    {
                        return true;
                    }
                    return false;

                default:
                    return false;
            }

        }
    }
}