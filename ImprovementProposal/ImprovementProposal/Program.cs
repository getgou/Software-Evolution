using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace ImprovementProposal
{
    class Program
    {
        static void Main(string[] args)
        {

            Console.Write("Enter the path to the ROOT folder of you project: ");
            string root = Console.ReadLine();
            //string root = "E:\\chalmers\\Y2-period1-2017\\SW.EV\\Project\\2017Group6\\android";
            Console.Write("Enter the path to the MAIN folder of you project (..\\app\\src\\main): ");
            string mainFolder = Console.ReadLine();
           // string mainFolder = "E:\\chalmers\\Y2-period1-2017\\SW.EV\\Project\\Individual\\open-event-orga-app-development\\app\\src\\main";
            // string root = "C:\\Users\\axel_\\OneDrive\\Dokument\\Chalmers\\Software Evolution Project\\OctoPrint-AndroidApp-1.0.0\\PrinterApp\\app\\src\\main\\java";
            Console.Write("Enter the path to the DRAWABLE folder of you project (..\\app\\src\\main\\res\\drawable): ");
            string drawablePath = Console.ReadLine();
            //string drawablePath = "E:\\chalmers\\Y2-period1-2017\\SW.EV\\Project\\Individual\\open-event-orga-app-development\\app\\src\\main\\res\\drawable";
            //string drawablePath = "E:\\chalmers\\Y2-period1-2017\\SW.EV\\Project\\2017Group6\\android\\app\\src\\main\\res\\drawable";

            string[] filesInProj = fetchProjFolders(mainFolder);
                                                              
            Dictionary<string, List<string>> resultList = new Dictionary<string, List<string>>();
            Console.WriteLine("Working...");

            foreach (var drawableFile in fetchResources(drawablePath))
            {
                List<string> usedInFiles = new List<string>();
                foreach (var file in filesInProj)
                {
                    string fileName = Path.GetFileName(file);
                    string drawableName = Path.GetFileNameWithoutExtension(drawableFile);

                    if (Path.GetExtension(file) == ".java")
                    {
                        if (File.ReadLines(file).Any(line => line.Contains("R.drawable." + drawableName) &&
                                (Path.GetFileNameWithoutExtension(file) != drawableName)))
                        {
                            usedInFiles.Add(fileName);
                        }
                    }
                    else if (Path.GetExtension(file) == ".xml")
                    {
                        if (File.ReadLines(file).Any(line => (line.Contains("\"@drawable/" + drawableName + "\"") ||
                                line.Contains("@drawable/" + drawableName))) &&
                                (Path.GetFileNameWithoutExtension(file) != drawableName))
                        {
                            usedInFiles.Add(fileName);
                        }
                    }
                }
                if (usedInFiles.Count > 0)
                {
                    resultList.Add(drawableFile, usedInFiles);
                }
            }

            if (resultList.Count == 0)
            {
                Console.WriteLine("No drawables used in project!");
            }
            var lineToAdd = "";
            using (StreamWriter file = new StreamWriter(root + "\\improvementResult.txt"))
            {
                foreach (var item in resultList)
                {
                    Console.WriteLine("Drawable " + "'" + Path.GetFileName(item.Key) + "'" + " found in files: ");

                    file.WriteLine("Drawable " + "'" + Path.GetFileName(item.Key) + "'" + " found in files: ");

                    lineToAdd += "<li><i class=\"fa fa-plus\"></i> <label>";
                    lineToAdd += Path.GetFileName(item.Key) + "</label>";
                    if (item.Value.Count > 0)
                    {
                        lineToAdd += "<ul>";
                    }
                    foreach (var row in item.Value)
                    {
                        Console.Write(Path.GetFileName(row) + ",");
                        file.Write(Path.GetFileName(row) + ",");
                        if (item.Value.Count > 0)
                        {
                            lineToAdd += "<li><label>" + Path.GetFileName(row) + "</label></li>";
                        }
                    }
                    if (item.Value.Count > 0)
                    {
                        lineToAdd += "</ul>";

                    }
                    lineToAdd += "</li>";
                    Console.WriteLine("");
                    Console.WriteLine("");
                    file.WriteLine("");
                    file.WriteLine("");
                }
            }
            //
            lineToAdd += "</ul>";
            // var fileName1 = root + "\\output\\index.html";
            string rootProposalFolder = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.FullName;
            string fileName1 = rootProposalFolder + "\\res\\index.html";
			Debug.Print("filename:" + fileName1);
            var txtLines = File.ReadAllLines(fileName1).ToList();   //Fill a list with the lines from the txt file.
            txtLines.Insert(40, lineToAdd);  //Insert the line you want to add last under the tag 'item1'.

            Directory.CreateDirectory(rootProposalFolder + "\\output");

            using (StreamWriter sm = new StreamWriter(rootProposalFolder + "\\output\\output.html"))
            {
                foreach(var line in txtLines)
                {
                    sm.WriteLine(line);
                }
            } 

              //  File.WriteAllLines(rootProposalFolder + "\\output\\output.html", txtLines);
            //

            Console.ReadKey();
        }


        private static string[] fetchProjFolders(string mainFolder)
        {
            string[] files = Directory.GetFiles(mainFolder, "*", SearchOption.AllDirectories);
            return files;
        }

        private static List<string> fetchResources(string path)
        {
            List<string> fileNames = new List<string>();

            string[] files = Directory.GetFiles(path, "*", SearchOption.AllDirectories);

            foreach (var file in files)
            {
                fileNames.Add(Path.GetFileNameWithoutExtension(file));
            }
            return fileNames;
        }
    }
}