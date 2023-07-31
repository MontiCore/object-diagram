# SLE Project: OD & AD to PlantUML PrettyPrinter

This document explains the organization of the project.

## Meetings, dates, deadlines

Following table shows the general plan for the course, it is not strict and is here to provide an overview.

| Start  | Implementation | Report             | End                  |
|--------|----------------|--------------------|----------------------|
| ~01.06 | ~6 weeks       | 03.07              | 18.08                |
| intro  | tasks          | final presentation | finish documentation |

* Meetings take place every week
* Preferred location: Informatik-Zentrum, E1, 3 OG, Smartroom
* Alternative via [Zoom](https://us04web.zoom.us/j/3900225028?pwd=MWg2U201VmlaUk5RYVRmd1AveXhMdz09)
* Each meeting takes ~30 minutes
* Extra meetings can be arranged if needed

## Overall Tasks

1. Implement a pretty printer
   for [Object Diagram (OD)](https://git.rwth-aachen.de/se-student/ss23/lectures/sle/student-projects/plantuml-pretty-printers/od)
   and [Activity Diagram (AD)](https://git.rwth-aachen.de/se-student/ss23/lectures/sle/student-projects/plantuml-pretty-printers/adlanguage)
   languages models that creates [wireframe](https://plantuml.com/de/salt).
    1. evaluate if PlantUML's [legacy](https://plantuml.com/de/activity-diagram-legacy)
       or [beta](https://plantuml.com/de/activity-diagram-beta) AD language is better suited.
    2. create suitable test cases for your pretty printer
    3. ensure that the visualization style comes as close to the chair's [diagram style](diagram_style.pptx) as possible
2. Create a tool class for meaningful methods for... _(more information next meeting)_
    1. ...printing OD and AD models to `puml`-files.
    2. ...rendering (printed) models.
    3. create suitable test cases
3. Create a simple CLI library to provide functionality 2 i. and ii.
4. Report
    * write JavaDoc for the written classes and methods
    * create a Markdown document on how to use the PlantUML tools
    * write an abstract/ introduction with a transition to your Markdown documentation

## Workflow

Some general notes on how we will work:

* Tasks are given each week by the supervisor
* Most of the work is done in GitLab, as soon as you are done with some little task, sub-task, push
  the result to your project
* Communication primarily via Slack
* You may get e-mails from the supervisor, so check your e-mails
* Everything is negotiable, communicating with the supervisor too much is better than too little

## Evaluation

The evaluation of your work is done by the supervisor.

Factors with most negative impact (from worst things to do to least bad):

* Not doing anything
* Not pushing anything to GitLab
* Not communicating project-related issues, e.g., that you will not be able to come to a meeting
* Not pushing anything to GitLab before a deadline
* Not addressing supervisor's suggestions and corrections in any way

Factors with no impact:

* Mistakes, bugs in code
* Not knowing and asking something
* Giving negative feedback to the supervisor

Factors with positive impact (no order):

* Holding deadlines
* Reasonable task completion
* Simple and functioning implementation for the corresponding tasks
* Reasonably using other libraries/tools to complete your task (don't break NDA), e.g., Chat GPT