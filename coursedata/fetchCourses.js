const axios = require('axios');
// to write coursedata to a file
const fs = require("fs");

// the sessions we want to pull data for, yyyym
const current_sessions = ["20259", "20261", "20259-20261"];

// updated pageSize to 9000, taken from ttb 
const payload = {
  courseCodeAndTitleProps: {
    courseCode: "",
    courseTitle: "",
    courseSectionCode: ""
  },
  departmentProps: [],
  campuses: [],
  sessions: current_sessions,
  requirementPrhops: [],
  instructor: "",
  courseLevels: [],
  deliveryModes: [],
  dayPreferences: [],
  timePreferences: [],
  divisions: ["APSC", "ARTSC", "FIS", "FPEH", "MUSIC", "ARCLA", "ERIN", "SCAR"],
  creditWeights: [],
  availableSpace: false,
  waitListable: false,
  page: 1,
  pageSize: 9000,
  direction: "asc"
};

async function fetchCourses() {
  try{
    const response = await axios.post('https://api.easi.utoronto.ca/ttb/getPageableCourses', 
      payload, 
      {headers: { 'Content-Type': 'application/json' }
      });
    console.log(JSON.stringify(response.data, null, 4));
    return response.data;
  }
  catch(err){
    if (err.response) {
      console.error('Request failed with status', err.response.status);
    }
  }
}


async function main() {
  // Get course data 
  const responsedata = await fetchCourses();
  const finalresponseData = JSON.stringify(responsedata, null, 4); 

  // Specify the file path to will save courses in 
  const datafilePath = "fetchedcourses.json";

  // Write the JSON string to the file
  fs.writeFile(datafilePath, finalresponseData,(err) => {
    if (err) {
      console.error('Error writing to file:', err);
      return;
    }
    console.log('Coursedata saved to', datafilePath);
  });
}

main();