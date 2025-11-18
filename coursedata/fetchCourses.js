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

// async function fetchCourses() {
//   try{
//     const response = await axios.post(
//       'https://api.easi.utoronto.ca/ttb/getPageableCourses', 
//       payload, 
//         headers: { 'Content-Type': 'application/json' }
//       });
//   console.log(JSON.stringify(response.data, null, 4));
//   }

//   catch(err){
//     if (err.response) {
//       console.error('Request failed with status', err.response.status);
//     }
//   }
// }

async function fetchAllCourses() {
  let allPages = [];
  // const stream = fs.createWriteStream('allfetchedcourses.json');
  let hasmorecourses = true; 

  try {
    // stream.write('[');
    let isFirst = true;

    while (hasmorecourses) {
      console.log(`FETCHING ${payload.page}...`);

      const response = await axios.post(url,
        payload, 
        { headers: { 'Content-Type': 'application/json' }}
      );
      const data = response.data;
      allPages.push(...data.payload.pageableCourse.courses);
      hasmorecourses = data.payload.pageableCourse.courses.length != 0;

      if (!hasmorecourses) {console.log(`No more courses to fetch.`)}
      payload.page++;
    } 
  } catch (err) { 
    console.error('Error fetching courses:', err.message);
  }
  restructureCourses(allPages);
}

function restructureCourses(allPages) {
  try {
    const coursesBySubject = {};
    
    allPages.forEach(course => {
      // Extract subject code "EUR")
      const subjectCode =  course.code.substring(0, 3); 
      
      // Create the full key for this specific instance
      const fullKey = `${course.code}-${course.sectionCode}-${course.sessions[0]}`;
      
      const convertedCourse = convertToOldFormat(course);
      
      // Initialize the subject group if it doesn't exist
      if (!coursesBySubject[subjectCode]) {
        coursesBySubject[subjectCode] = {};
      }
      
      // Add converted course instance under its full key
      coursesBySubject[subjectCode][fullKey] = convertedCourse;
    });

    for (const [subjectCode, instances] of Object.entries(coursesBySubject)) {
      const filename = `courses/${subjectCode}.json`;
      fs.writeFileSync(filename, JSON.stringify(instances, null, 2));
      console.log(`Saved ${subjectCode}.json with ${Object.keys(instances).length} courses`);
    }
    console.log(`✓ Created ${Object.keys(coursesBySubject).length} subject files`);
  } catch (err) {
    console.error('Error:', err.message);
  }
}
   
