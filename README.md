# revature_project_2
Edward Reed, Danny Lee, and Nahshon William's project 2 for the Big Data October 2020 branch


### Project 2
This is a template, to be filled out by each group and placed in their git repo titled README.md
Requirements:
- Create a Spark Application that processes Twitter data
- Your project 2 pitch should involve some analysis of twitter data.  This can be the central feature.  Your final application should work, to some extent, with both streaming and historical data.
- Send me a link to a git repo, have someone in the group manage git (but they can ask for help from me)
- Produce a one or more .jar files for your analysis.  Multiple smaller jar files are preferred.

### Presentations
- Bring a simple slide deck providing an overview of your results.  You should present your results, a high level overview of the process used to achieve those results, and any assumptions and simplifications you made on the way to those results.
- I may ask you to run an analysis on the day of the presentation, so be prepared to do so.
- We'll have 20 minutes per group, so make sure your presentation can be covered in that time, focusing on the parts of your analysis you find most interesting.
- Include a link to your github repository at the end of your slides

### Technologies
- Apache Spark
- Spark SQL
- YARN
- HDFS and/or S3
- Scala 2.12.10
- Git + GitHub

### Due Date
- Presentations will take place on Monday, 11/23

### Questions
Q1: Can we identify patterns in trending topics being popular in one location and then moving to others?  
Q2: If so, can we identify separate regions for these patterns? ex.: North American patterns, East Asian patterns, etc.  
Q3: What about global scale patterns for specific events? ex.: Burning of Notre Dame-how did this topic travel according to twitter usage?  

### Instructions

* To stream Twitter trend data using Spark streaming, clone he `Project-2 @ 5748ae0` repository.
* To convert Twitter trend API response to "trend objects" for analysis, clone the `transformer-pj2 @ 9359f89` repository.
* To do analysis on the "trend objects", clone this repository and cd into `Simplified-trending` repository.

There is a rather large (409K) list of trend objects available as `processed_trends_full_dataset.csv` if you would like to move straight to analaysis.  Just copy the file into the Simplified-trending directory and designate it as the input file.

There are links to sample (unprocessed data) below.  

### Raw data
- Sample data (gDrive): [sample-trend-data-11_13-19_20202.tar.gz](https://drive.google.com/file/d/1fN3BjOMfke32r7TVxRaNu-1yT9JtYYah/view?usp=sharing) 823kb gzipped, 13mb unzipped.
- Larger sample data set (gDrive):  [sample-trend-data_larger_response_set.tar.gz](https://drive.google.com/file/d/1DHmJCIs2r4OK4BBj6d-uFtGiFX1jSLNO/view?usp=sharing) 2mb gzipped, 54mb unzipped.
- Consolidated all data to one file (gDrive): [sample_trend_data_whole_set.tar.gz](https://drive.google.com/file/d/1jeaPTZlcY1JbZh3W_J6T1Xv1M09eAXrp/view?usp=sharing) 3.2mb,  67mb unzipped.

