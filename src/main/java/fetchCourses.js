const axios = require('axios')

// the sessions we want to pull data for
const current_sessions = ["20259", "20261", "20259-20261"]

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
  pageSize: 9000,
  direction: "asc"
};

axios({
  method: 'post',
  url: 'https://api.easi.utoronto.ca/ttb/getPageableCourses',
  data: payload
});