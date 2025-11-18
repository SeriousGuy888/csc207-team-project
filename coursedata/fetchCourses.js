const axios = require('axios');
const fs = require("fs");


const current_sessions = ["20259", "20261", "20259-20261"];
const url = 'https://api.easi.utoronto.ca/ttb/getPageableCourses'

const payload = {
  courseCodeAndTitleProps: {
    courseCode: "",
    courseTitle: "",
    courseSectionCode: ""
  },
  departmentProps: [],
  campuses: [],
  sessions: current_sessions,
  requirementProps: [],
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
  pageSize: 20,
  direction: "asc"
};

async function fetchCourses() {
  try{
    const response = await axios.post(
      'https://api.easi.utoronto.ca/ttb/getPageableCourses', 
      payload, 
      {
        headers: { 'Content-Type': 'application/json' }
      });
  console.log(JSON.stringify(response.data, null, 4));
  fs.writeFile("C:\\Users\\carol\\Downloads\\csc207-team-project-1\\coursedata\\nofetchedcourses.json", JSON.stringify(response.data, null, 4), (err))
  }

  catch(err){
    if (err.response) {
      console.error('Request failed with status', err.response.status);
    }
  }
}

async function fetchAllCourses() {
  const stream = fs.createWriteStream('allfetchedcourses.json');
  let hasmorecourses = true; 

  try {
    stream.write('[');
    let isFirst = true;

    while (hasmorecourses) {
      console.log(`FETCHING ${payload.page}...`);

      const response = await axios.post(url,
        payload, 
        { headers: { 'Content-Type': 'application/json' }}
      );
      const data = response.data;
      console.log(JSON.stringify(data, null, 2));

      // save it to fetchedcourses.json by page 
      if (!isFirst) {stream.write(',\n')};

      console.log(`Saving page ${payload.page}...`);
      stream.write(JSON.stringify(data, null, 2) + '\n');

      isFirst = false;
      hasmorecourses = data.payload.pageableCourse.courses.length != 0;

      if (!hasmorecourses) {console.log(`No more courses to fetch.`)}
      payload.page++;
    }

    stream.write(']');
    stream.end();
    console.log('All courses fetched and saved to fetchedcourses.json');  
  } catch (err) { 
    stream.end();
    console.error('Error fetching courses:', err.message);
  }
}

fetchAllCourses();