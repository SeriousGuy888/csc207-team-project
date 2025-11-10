# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.

## Group Members

- Victoria Cai
- Bardiya Momeni
- Phoebe Kuang
- Caroline Lyu
- Bill Huang
- Abish Kulkarni

## Design

[Figma Project](https://www.figma.com/design/1ZN5LvzQbgdHj9iV520tBF/UI-Design?node-id=0-1&t=uXEwq13twGXqDEwg-1)

We aim to create a user interface that looks a little bit like this:

![](/images/ui-overview.png)

## Terminology

For the sake of consistency across the project, we will use these terms to refer to the things in the project and UI:

- A **workbook** refers to one "project file" that the user is working on. Each workbook can contain multiple timetables that the user is working on.
  - That is, when a user "saves" or "loads" their work, the file being saved or loaded represents a workbook.
  - The entire window is dedicated to displaying one workbook at a time.
    - This is analogous to Microsoft Excel workbooks.
- A **timetable** refers to a collection of course sections chosen by the user.
  - It _includes_ potentially multiple semesters.
  - In the UI, it is represented as a single tab.
    - This is analogous to Microsoft Excel worksheets.
  - In the code, it is modelled by the `Timetable` entity class.
  - In our planning stages, this was sometimes referred to as a "prototype" or a "canvas".
- A **search result** is represented in the UI by one accordion dropdown entitled with the course code and session. This accordion further contains a list of section types (e.g. lectures, tutorials). Each section type contains a list of sections of that type, which the user can add to their timetable.

