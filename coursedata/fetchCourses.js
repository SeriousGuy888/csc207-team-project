const axios = require('axios');

// the sessions we want to pull data for, yyyym
const current_sessions = ["20259", "20261", "20259-20261"];

// updated pageSize to 9000
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

    console.log(JSON.stringify(response.data));
  }
  catch(err){
    if (err.response) {
      console.error('Request failed with status', err.response.status);
    }
  }
}

fetchCourses();