using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Swerea_API.Models;
using System.Xml;
using Assimp;
using System.IO;
using Amazon.S3;
using Kaitai;
using Amazon.S3.Transfer;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
namespace Swerea_API.utils
{
    public class CostEstimater
    {

        static readonly string ACCESSKEY = "AKIAJ65MDN6EGP5DUGNQ";
        static readonly string SECRETKEY = "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";
        static readonly string BUCKETNAME = "swereabucket";

        //Get the relative project path and download paths
        static readonly string path = Path.GetFullPath(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, @"..\..\"));
        static readonly string stlLocation = path + @"downloads\stl\";
        static readonly string jsonLocation = path + @"downloads\json\";
        static readonly string downloadDirectory = path + @"downloads\";
        //used for stl file conversion
        static AssimpContext assimpContext = new AssimpContext();
        //setting up amazons3 client to download stl files
        static TransferUtility fileTransferUtility = new TransferUtility(new AmazonS3Client(ACCESSKEY, SECRETKEY, Amazon.RegionEndpoint.EUCentral1));
        public static void EstimateCost()
        {
            if (!Directory.Exists(path + @"downloads\"))
            {
                Directory.CreateDirectory(downloadDirectory);
                Directory.CreateDirectory(stlLocation);
                Directory.CreateDirectory(jsonLocation);
            }
            DownloadAllSTLs();
        }

        public static CostEstimation Cost(int buildId, string fileName)
        {
            EstimateCost();
            double zHeight = ConvertSingleFile(Path.Combine(stlLocation, fileName), Path.Combine(jsonLocation + fileName + @".obj"));
            int numberOfLayers = GetNumberOfLayers(zHeight);
            int printTime = GetPrintingTime(numberOfLayers);
            int totalPowderUsed = GetTotalPowderUsed(printTime);
            int cost = GetCostEstimate(totalPowderUsed);
            CostEstimation c = new CostEstimation(buildId, fileName, zHeight, numberOfLayers, printTime, cost, totalPowderUsed);
            return c;
        }

        public static void DownloadAllSTLs()
        {
            fileTransferUtility.DownloadDirectory(BUCKETNAME, "stl", stlLocation);
        }

        public static void UploadAllJsons()
        {
            fileTransferUtility.UploadDirectory(jsonLocation, BUCKETNAME, @"*.json", SearchOption.TopDirectoryOnly);
            fileTransferUtility.UploadDirectory(downloadDirectory, BUCKETNAME, @"*.json", SearchOption.AllDirectories);
        }

        public static void ConvertAllFiles()
        {
            string[] files = Directory.GetFiles(stlLocation, @"*.stl");
            foreach (string file in files)
            {
                Console.WriteLine(file);
                string outputFile2 = jsonLocation + Path.GetFileNameWithoutExtension(file) + @".obj";
                ConvertSingleFile(file, outputFile2);
            }
        }

        public static void ConvertFile(string inputFile, string outputFile)
        {
            CostEstimation costEstimation = new CostEstimation();
            double totalZHeight;
            int numberOfLayers;
            int printTime;
            double costEstimate;
            //to check whether file can be converted or not
            bool conversion = assimpContext.ConvertFromFileToFile(inputFile, outputFile, "collada");
            //treating dae (collada) file extension as if it is XML
            XmlDocument dae = new XmlDocument();
            dae.Load(outputFile);
            //This node contains the Z values
            XmlNode meshes = dae.SelectSingleNode("//*[@id='meshId0-positions-array']");
            if (conversion)
            {
                if (meshes.InnerText != "")
                {
                    //here we put every value in string array
                    string values = meshes.InnerText;
                    string[] heights = values.Split(' ');
                    double[] zHeights = new double[heights.Length];
                    //c# does not like it when it finds an empty value, so we replace it with a i
                    for (int i = 0; i < heights.Length; i++)
                    {
                        if (string.IsNullOrEmpty(heights[i]))
                        {
                            heights[i] = i.ToString();
                        }
                        if (Double.TryParse(heights[i], out zHeights[i]))
                        {
                            zHeights[i] = Double.Parse(heights[i]);
                        }

                    }

                    totalZHeight = Math.Round(getMostFrequentValue(zHeights), 3);
                    numberOfLayers = GetNumberOfLayers(totalZHeight);
                    printTime = GetPrintingTime(numberOfLayers);
                    costEstimate = GetCostEstimate(printTime);
                }
                else
                {
                    Console.WriteLine("No meshes found!");
                }
            }
            else
            {
                Console.WriteLine("File could not be converted, either damaged or has already been converted");
            }
        }

        //this function (relationship) was extracted from tableau with R squared of 0.98
        public static int GetNumberOfLayers(double zHeight)
        {
            int numberOfLayers = Convert.ToInt32((zHeight * 33.33) + 0.5);
            return numberOfLayers;
        }

        //this function (relationship) was extracted from tableau with R squared of 0.68
        public static int GetCostEstimate(int totalPowderUsed)
        {
            int costEstimate = Convert.ToInt32((1.59232 * totalPowderUsed) + 12917.9);
            return costEstimate;
        }

        //this function (relationship) was extracted from tableau with R squared of 0.75
        public static int GetPrintingTime(int numberOfLayers)
        {
            double printingTimeTemp = Math.Pow((0.792 * numberOfLayers), 0.953);
            int printingTime = Convert.ToInt32(printingTimeTemp);
            return printingTime;
        }

        //this function (relationship) was extracted from tableau with R squared of 0.77
        public static int GetTotalPowderUsed(int printTime)
        {
            double tempLog = Math.Log(printTime);
            double result = (5699.05 * tempLog) - 27124.8;
            int totalPowerUsed = Convert.ToInt32(result);
            return totalPowerUsed;
        }

        static int countOccurrences(double[] list, double targetValue)
        {
            int count = 0;
            for (int i = 0; i < list.Length; i++)
            {
                if (list[i] == targetValue)
                    count++;
            }
            return count;
        }

        static double getMostFrequentValue(double[] list)
        {
            int mostFrequentCount = 0;
            double mostFrequentValue = 0;
            for (int i = 0; i < list.Length; i++)
            {
                double value = list[i];
                int count = countOccurrences(list, value);
                if (count > mostFrequentCount)
                {
                    mostFrequentCount = count;
                    mostFrequentValue = value;
                }
            }
            return mostFrequentValue;
        }



        public static double ConvertSingleFile(string inputFile, string outputFile)
        {
            assimpContext.ConvertFromFileToFile(inputFile, outputFile, "obj");
            string[] lines = File.ReadAllLines(outputFile);
            List<double> listValues = new List<double>();
            double sum;
            foreach (string line in lines)
            {
                if (line.StartsWith("v "))
                {
                    string[] temp = line.Split(' ');
                    List<string> temp2 = new List<string>();
                    foreach (string t in temp)
                    {
                        if (t.Contains("."))
                        {
                            int index = t.IndexOf('.');
                            if (index < t.Length - 4)
                            {
                                temp2.Add(t.Remove(index + 4));
                            }
                        }
                    }
                    for (int i = 0; i < temp2.Count; i++)
                    {
                        double val;
                        if (Double.TryParse(temp2.ElementAt(i), out val))
                        {
                            listValues.Add(Math.Abs(val));
                        }
                    }
                }
            }
            sum = listValues.Sum();
            double totalZHeight = sum / listValues.Count;
            return totalZHeight;
        }
    }
}

/// <summary>
/// STL files are used to represent simple 3D models, defined using
/// triangular 3D faces.
/// 
/// Initially it was introduced as native format for 3D Systems
/// Stereolithography CAD system, but due to its extreme simplicity, it
/// was adopted by a wide range of 3D modelling, CAD, rapid prototyping
/// and 3D printing applications as the simplest 3D model exchange
/// format.
/// 
/// STL is extremely bare-bones format: there are no complex headers, no
/// texture / color support, no units specifications, no distinct vertex
/// arrays. Whole model is specified as a collection of triangular
/// faces.
/// 
/// There are two versions of the format (text and binary), this spec
/// describes binary version.
/// </summary>
public partial class Stl : KaitaiStruct
{
    public static Stl FromFile(string fileName)
    {
        return new Stl(new KaitaiStream(fileName));
    }

    public Stl(KaitaiStream p__io, KaitaiStruct p__parent = null, Stl p__root = null) : base(p__io)
    {
        m_parent = p__parent;
        m_root = p__root ?? this;
        _read();
    }
    private void _read()
    {
        _header = m_io.ReadBytes(80);
        _numTriangles = m_io.ReadU4le();
        _triangles = new List<Triangle>((int)(NumTriangles));
        for (var i = 0; i < NumTriangles; i++)
        {
            _triangles.Add(new Triangle(m_io, this, m_root));
        }
    }

    /// <summary>
    /// Each STL triangle is defined by its 3 points in 3D space and a
    /// normal vector, which is generally used to determine where is
    /// &quot;inside&quot; and &quot;outside&quot; of the model.
    /// </summary>
    public partial class Triangle : KaitaiStruct
    {
        public static Triangle FromFile(string fileName)
        {
            return new Triangle(new KaitaiStream(fileName));
        }

        public Triangle(KaitaiStream p__io, Stl p__parent = null, Stl p__root = null) : base(p__io)
        {
            m_parent = p__parent;
            m_root = p__root;
            _read();
        }
        private void _read()
        {
            _normal = new Vec3d(m_io, this, m_root);
            _vertices = new List<Vec3d>((int)(3));
            for (var i = 0; i < 3; i++)
            {
                _vertices.Add(new Vec3d(m_io, this, m_root));
            }
        }
        private Vec3d _normal;
        private List<Vec3d> _vertices;
        private Stl m_root;
        private Stl m_parent;
        public Vec3d Normal { get { return _normal; } }
        public List<Vec3d> Vertices { get { return _vertices; } }
        public Stl M_Root { get { return m_root; } }
        public Stl M_Parent { get { return m_parent; } }
    }
    public partial class Vec3d : KaitaiStruct
    {
        public static Vec3d FromFile(string fileName)
        {
            return new Vec3d(new KaitaiStream(fileName));
        }

        public Vec3d(KaitaiStream p__io, Stl.Triangle p__parent = null, Stl p__root = null) : base(p__io)
        {
            m_parent = p__parent;
            m_root = p__root;
            _read();
        }
        private void _read()
        {
            _x = m_io.ReadF4le();
            _y = m_io.ReadF4le();
            _z = m_io.ReadF4le();
        }
        private float _x;
        private float _y;
        private float _z;
        private Stl m_root;
        private Stl.Triangle m_parent;
        public float X { get { return _x; } }
        public float Y { get { return _y; } }
        public float Z { get { return _z; } }
        public Stl M_Root { get { return m_root; } }
        public Stl.Triangle M_Parent { get { return m_parent; } }
    }
    private byte[] _header;
    private uint _numTriangles;
    private List<Triangle> _triangles;
    private Stl m_root;
    private KaitaiStruct m_parent;
    public byte[] Header { get { return _header; } }
    public uint NumTriangles { get { return _numTriangles; } }
    public List<Triangle> Triangles { get { return _triangles; } }
    public Stl M_Root { get { return m_root; } }
    public KaitaiStruct M_Parent { get { return m_parent; } }
}