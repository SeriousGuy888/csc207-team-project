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
  let hasmorecourses = true; 

  try {
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
      
      const convertedCourse = convertFormat(course);
      
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
    console.log(`Created ${Object.keys(coursesBySubject).length} subject files`);
  } catch (err) {
    console.error('Error:', err.message);
  }
}
   
function convertFormat(course) {
  const NewCourseFormat = {
    courseId: course.id,
    // org: course.department?.code || "",
    // orgName: course.department?.name || "",
    courseTitle: course.name,
    code: course.code,
    courseDescription: cleanHTML(course.cmCourseInfo?.description ?? ""),
    prerequisite: cleanHTML(course.cmCourseInfo?.prerequisitesText ?? ""),
    corequisite: cleanHTML(course.cmCourseInfo?.corequisitesText ?? ""),
    exclusion: cleanHTML(course.cmCourseInfo?.exclusionsText ?? ""),
    recommendedPreparation: cleanHTML(course.cmCourseInfo?.recommendedPreparation ?? ""),
    section: course.sectionCode,
    session: course.sessions[0] || "",
    webTimetableInstructions: cleanHTML(course.notes?.find(n => n.type === 'COURSE')?.content || ""),
    deliveryInstructions: null,
    breadthCategories: course.cmCourseInfo?.breadthRequirements?.join(", ") || "",
    distributionCategories: course.cmCourseInfo?.distributionRequirements?.join(", ") || "",
    meetings: getmeetings(course)
  };
  return NewCourseFormat;
}

function getmeetings(course) {
  // Convert sections to meetings
  if (course.sections) {
    meetings = {}
    course.sections.forEach(section => {
      const meetingKey = `${section.teachMethod}-${section.sectionNumber}`;
      
      meetings[meetingKey] = {
        schedule: convertSchedule(section.meetingTimes),
        instructors: convertInstructors(section.instructors),
        meetingId: section.name,
        teachingMethod: section.teachMethod,
        sectionNumber: section.sectionNumber,
        subtitle: section.subTitle || "",
        cancel: section.cancelInd === 'Y' ? 'Cancelled' : "",
        waitlist: section.waitlistInd,
        deliveryMode: section.deliveryModes?.[0]?.mode || "CLASS",
        online: section.deliveryModes?.[0]?.mode === 'INPER' ? 'In Person' : 'Online',
        enrollmentCapacity: String(section.maxEnrolment),
        actualEnrolment: String(section.currentEnrolment),
        actualWaitlist: String(section.currentWaitlist),
        enrollmentIndicator: section.enrolmentInd,
        meetingStatusNotes: cleanHTML(section.notes?.find(n => n.type === 'SECTION')?.content || null),
        enrollmentControls: convertEnrollmentControls(section.enrolmentControls),
        linkedMeetingSections: section.linkedMeetingSections ?? []
      };
    });
  }
  return meetings;
}

function convertSchedule(meetingTimes) {
  if (!meetingTimes || meetingTimes.length === 0) {
    return { 
      "-": {
        meetingDay: null,
        meetingStartTime: null,
        meetingEndTime: null,
        meetingScheduleId: null,
        assignedRoom1: null,
        assignedRoom2: null
      }
    };
  }

  const schedule = {};
  const days = ['SU', 'MO', 'TU', 'WE', 'TH', 'FR', 'SA'];
  
  meetingTimes.forEach((mt, idx) => {
    const day = days[mt.start.day];
    const startTime = millisToTime(mt.start.millisofday);
    const endTime = millisToTime(mt.end.millisofday);
    const room = mt.building?.buildingCode || "";
    const sessioncode = mt.sessionCode
    
    const key = `${day}-${idx}`;
    
    schedule[key] = {
      meetingDay: day,
      meetingStartTime: startTime,
      meetingEndTime: endTime,
      meetingScheduleId: String(idx),
      assignedRoom1: room,
      assignedRoom2: room,
      sessioncode: sessioncode
    };
  });
  
  return schedule;
}

function millisToTime(millis) {
  const hours = Math.floor(millis / 3600000);
  const minutes = Math.floor((millis % 3600000) / 60000);
  return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
}

function convertInstructors(instructors) {
  if (!instructors || instructors.length === 0) return {};
  
  const result = {};
  instructors.forEach((inst, idx) => {
    result[String(idx)] = {
      instructorId: String(idx),
      firstName: inst.firstName,
      lastName: inst.lastName
    };
  });
  return result;
}

function convertEnrollmentControls(controls) {
  if (!controls) return [];
  
  return controls.map(ctrl => ({
    postId: String(ctrl.sequence || ""),
    postCode: ctrl.post?.code || "",
    postName: ctrl.post?.name || "",
    subjectId: "1",
    subjectCode: ctrl.subject?.code || "*",
    subjectName: ctrl.subject?.name || "",
    designationId: "1",
    designationCode: ctrl.designation?.code || "*",
    designationName: ctrl.designation?.name || "",
    yearOfStudy: ctrl.yearOfStudy || "*",
    typeOfProgramId: "1",
    typeOfProgramCode: ctrl.typeOfProgram?.code || "*",
    typeOfProgramName: ctrl.typeOfProgram?.name || "All Types",
    primaryOrgId: "1",
    primaryOrgCode: ctrl.primaryOrg?.code || "*",
    primaryOrgName: ctrl.primaryOrg?.name || "",
    secondaryOrgId: "1",
    secondaryOrgCode: ctrl.secondOrg?.code || "*",
    secondaryOrgName: ctrl.secondOrg?.name || "",
    assocOrgId: "1",
    assocOrgCode: ctrl.associatedOrg?.code || "*",
    assocOrgName: ctrl.associatedOrg?.name || "",
    adminOrgId: "1",
    adminOrgCode: ctrl.adminOrg?.code || "*",
    adminOrgName: ctrl.adminOrg?.name || ""
  }));
}

function cleanHTML(text) {
  if (!text) return "";
  return text
    .replace(/<[^>]*>/g, '') // Remove all HTML tags
    .replace(/&nbsp;/g, ' ') // Replace &nbsp; with space
    .replace(/\s+/g, ' ')     // Replace multiple spaces with single space
    .trim();                  // Remove leading/trailing spaces
}


fetchAllCourses();