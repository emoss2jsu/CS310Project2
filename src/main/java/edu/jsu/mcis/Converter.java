package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import java.lang.Object;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class Converter {
	
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {	
		
		try {
			CSVReader reader = new CSVReader(new StringReader(csvString));
			List<String[]> list = reader.readAll();
			JSONObject json = new JSONObject();
				
			JSONArray cHeaders = new JSONArray();
			JSONArray rHeaders = new JSONArray();
			JSONArray data = new JSONArray();
				for (int i = 0; i < list.get(0).length; i++) 
					cHeaders.add(list.get(0)[i]);
				
		
				for (int i = 1; i < list.size(); i++) {
					rHeaders.add(list.get(i)[0]);
					data.add(Arrays.toString(Arrays.copyOfRange(list.get(i), 1 , list.get(i).length)));
				}
			
									
			json.put("colHeaders", cHeaders);
			json.put("rowHeaders", rHeaders);
			json.put("data", data);
			
			String jsonGrouped = "";
			jsonGrouped = "{\n    \"colHeaders\":" + json.get("colHeaders") + ",\n";
            jsonGrouped += "    \"rowHeaders\":" + json.get("rowHeaders") + ",\n";
			jsonGrouped += "    \"data\":" + 
							json.get("data").toString().replaceAll("\"", "").replaceAll("],", "],\n            ").replaceAll(", ", ",").replace("]]", "]\n    ]") 
							+ "\n}";
			
			return jsonGrouped;
		} catch (IOException e){};
		return null;
	
    }
    
    public static String jsonToCsv(String jsonString) {
    
		JSONObject json = null;
		try{
			JSONParser parser = new JSONParser();
			json = (JSONObject) parser.parse(jsonString);
		} catch (Exception e){}
		
		String csvString = (Converter.<String>joinArray((JSONArray) json.get("colHeaders")) + "\n");
		JSONArray rHeader = (JSONArray) json.get("rowHeaders");
		JSONArray data = (JSONArray) json.get("data");
		
			for(int i = 0; i < rHeader.size(); i++)
				csvString = (csvString + "\"" + (String)rHeader.get(i) + "\"," + 
					Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n");
			
			return csvString;
	}

	@SuppressWarnings("unchecked")
     private static <T> String joinArray(JSONArray arr) {
        String line = "";
        for (int i = 0; i < arr.size(); i++) {
            line += ("\"" + ((T) arr.get(i)) + "\"");
            if (i < arr.size() - 1) {
                line += ",";
            }
        }
	return line;
		
	}
	
}













