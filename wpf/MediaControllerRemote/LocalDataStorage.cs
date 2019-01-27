using System;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;

namespace MediaControllerRemote
{
    [Serializable]
    public class StoreData
    {
        //store last IP address
        public string IPAddress { get; set; }

        //store search info
        public int SelectedTypeIndex { get; set; }
        public string LastQuery { get; set; }

        //other staff
    }


    public class LocalDataStorage
    {
        public static StoreData data;

        public static void ReadData()
        {
            string path = GetDataPath();
            if (!File.Exists(path))
            {
                Fill();
                return;
            }

            try
            {
                IFormatter formatter = new BinaryFormatter();
                Stream stream = new FileStream(path, FileMode.Open, FileAccess.Read, FileShare.Read);
                data = (StoreData) formatter.Deserialize(stream);
                stream.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                File.Delete(path);
                Fill();
            }
        }

        private static void Fill()
        {
            data = new StoreData {IPAddress = "192.168.1.48", SelectedTypeIndex = 1, LastQuery = "GoGo Penguin"};
        }

        public static void WriteData()
        {
            IFormatter formatter = new BinaryFormatter();
            Stream stream = new FileStream(GetDataPath(), FileMode.Create, FileAccess.Write, FileShare.None);
            formatter.Serialize(stream, data);
            stream.Close();
        }

        private static string GetDataPath()
        {
            string currentDirectory = Directory.GetCurrentDirectory();
            string filePath = Path.Combine(currentDirectory, "StoreData.bin");
            return filePath;
        }
    }
}