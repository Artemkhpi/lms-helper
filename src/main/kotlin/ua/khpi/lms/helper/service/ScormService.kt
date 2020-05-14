package ua.khpi.lms.helper.service

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import org.zeroturnaround.zip.ZipUtil
import ua.khpi.lms.helper.model.ScormCourse
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.Test
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDateTime

@Service
class ScormService {
    fun createArchive(scormCourse: ScormCourse, articles: List<Article>, tests: List<Test>): String {
        val parentPath = LocalDateTime.now().toString().replace(":", "-").replace(".", "-")
        FileUtils.copyDirectory(File("scorm"), File(parentPath))
        val contentFilesNames = createContentFiles(articles, "$parentPath/main/")
        createManifestFile(scormCourse, parentPath, contentFilesNames)
        if (tests.isNotEmpty()) {
            createFileWithTests(tests, "$parentPath/main/")
        }
        createLaunchPage(contentFilesNames, "$parentPath/shared", tests.isNotEmpty())
        ZipUtil.pack(File(parentPath), File("$parentPath.zip"))
        FileUtils.deleteDirectory(File(parentPath))
        return "$parentPath.zip"
    }

    private fun createContentFiles(articles: List<Article>, parentPath: String): List<String> {
        val fileNames = mutableListOf<String>()
        articles.forEach { article ->
            article.content.forEach { contentPart ->
                fileNames.add(createHtmlFile(contentPart.htmlBody, "${article.title}_contentPart.${contentPart.partId}", parentPath))
            }
        }
        return fileNames
    }

    private fun createFileWithTests(tests: List<Test>, parentPath: String) {
        val questions = tests.flatMap { it.questions }
        val bodyBuilder = StringBuilder()
        for (i in 0 until questions.size) {
            val answers = questions[i].answers.joinToString(separator = ", ") { "\"${it.text}\"" }
            val correctAnswer = questions[i].answers.filter { it.correct }.first().text
            bodyBuilder.append("""test.AddQuestion( new Question ("com.scorm.golfsamples.interactions.main_${i + 1}",
                                "${questions[i].question}", 
                                QUESTION_TYPE_CHOICE,
                                new Array($answers),
                                "$correctAnswer",
                                "obj_main")
                );
""")
        }

        FileUtils.writeStringToFile(File("${parentPath}questions.js"), bodyBuilder.toString(), Charset.defaultCharset())
    }

    private fun createManifestFile(scormCourse: ScormCourse, parentPath: String, contentFilesNames: List<String>): String {
        val fileNamesPart = contentFilesNames.joinToString(separator = "\n      ") { "<file href=\"main/$it\"/>" }
        val manifestContent = """<?xml version="1.0" standalone="no" ?>
<manifest identifier="com.scorm.golfsamples.runtime.basicruntime.20043rd" version="1"
          xmlns="http://www.imsglobal.org/xsd/imscp_v1p1"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_v1p3"
          xmlns:adlseq="http://www.adlnet.org/xsd/adlseq_v1p3"
          xmlns:adlnav="http://www.adlnet.org/xsd/adlnav_v1p3"
          xmlns:imsss="http://www.imsglobal.org/xsd/imsss"
          xsi:schemaLocation="http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd
                              http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd
                              http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd
                              http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd
                              http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd">
	
  <metadata>
		<schema>ADL SCORM</schema>
		<schemaversion>2004 3rd Edition</schemaversion>
	</metadata>
  <organizations default="golf_sample_default_org">
		<organization identifier="golf_sample_default_org" adlseq:objectivesGlobalToSystem="false">
			<title>${scormCourse.title}</title>
			<item identifier="item_1" identifierref="resource_1">
				<title>${scormCourse.title}</title>
        <imsss:sequencing>
          <imsss:deliveryControls completionSetByContent="true" objectiveSetByContent="true"/>
        </imsss:sequencing>
			</item>
      <imsss:sequencing>
        <imsss:controlMode choice="true" flow="true"/>
      </imsss:sequencing>
		</organization>
	</organizations>
	<resources>
		<resource identifier="resource_1" type="webcontent" adlcp:scormType="sco" href="shared/launchpage.html">
      $fileNamesPart
      <file href="shared/assessmenttemplate.html"/>
      <file href="shared/background.jpg"/>
      <file href="shared/cclicense.png"/>
      <file href="shared/contentfunctions.js"/>
      <file href="shared/launchpage.html"/>
      <file href="shared/scormfunctions.js"/>
      <file href="shared/style.css"/>
		</resource>
	</resources>
</manifest>
"""
        FileUtils.writeStringToFile(File("$parentPath/imsmanifest.xml"), manifestContent, Charset.defaultCharset())
        return "imsmanifest.xml";
    }

    private fun createHtmlFile(htmlBody: String, pageTitle: String, parentPath: String): String {
        val body = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
<head>
    <title>$pageTitle</title>
    <style type="text/css" media="screen">
		@import url( ../shared/style.css );
	</style>
	<script src="../shared/scormfunctions.js" type="text/javascript"></script>
	<script src="../shared/contentfunctions.js" type="text/javascript"></script>
</head>
<body>
    <h1>$pageTitle</h1>
    $htmlBody
</body>
</html>
"""
        FileUtils.writeStringToFile(File("$parentPath/$pageTitle.html"), body, Charset.defaultCharset())
        return "$pageTitle.html";
    }

    private fun createLaunchPage(contentFilesNames: List<String>, parentPath: String, includeTestPage: Boolean): String {
        var fileNamesPart = contentFilesNames.joinToString(separator = "\n    ") { "pageArray[${contentFilesNames.indexOf(it)}] = \"main/$it\";" }
        if (includeTestPage) {
            fileNamesPart += "\n    pageArray[${contentFilesNames.size}] = \"shared/assessmenttemplate.html?questions=main\";"
        }
        val contentBody = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Course Launch Page</title>
    <script src="scormfunctions.js" type="text/javascript"></script>
    <script type="text/javascript">
    
    /*************************************/
    //functions for sizing the iFrame
    /*************************************/
    function setIframeHeight(id, navWidth) {
        if ( document.getElementById ) {
            var theIframe = document.getElementById(id);
            if (theIframe) {
                var height = getWindowHeight();
                theIframe.style.height = Math.round( height ) - navWidth + "px";
                theIframe.style.marginTop = Math.round( ((height - navWidth) - parseInt(theIframe.style.height) )/2 ) + "px";
            }
        }
    }

    function getWindowHeight() {
        var height = 0;
        if (window.innerHeight){
            height = window.innerHeight - 18;
        }
        else if (document.documentElement && document.documentElement.clientHeight){
            height = document.documentElement.clientHeight;
        }
        else if (document.body && document.body.clientHeight) {
            height = document.body.clientHeight;
        }
        return height;
    }
    
    function SetupIFrame(){
        //set our iFrame for the content to take up the full screen except for our navigation
        var navWidth = 40;
        setIframeHeight("contentFrame", navWidth);
        //need this in a setTimeout to avoid a timing error in IE6
        window.setTimeout('window.onresize = function() { setIframeHeight("contentFrame", ' + navWidth + '); }', 1000);
    }
    
    /*************************************/
    //content definition
    /*************************************/
    var pageArray = new Array(${if (includeTestPage) contentFilesNames.size + 1 else contentFilesNames.size});

    $fileNamesPart
    
    /*************************************/
    //navigation functions
    /*************************************/
    
    var currentPage = null;
    var startTimeStamp = null;
    var processedUnload = false;
    var reachedEnd = false;
    
    function doStart(){
        
        SetupIFrame();
        
        startTimeStamp = new Date();
        
        ScormProcessInitialize();

        var completionStatus = ScormProcessGetValue("cmi.completion_status", true);
        if (completionStatus == "unknown"){
            ScormProcessSetValue("cmi.completion_status", "incomplete");
        }
        
        //see if the user stored a bookmark previously (don't check for errors
        //because cmi.location may not be initialized
        var bookmark = ScormProcessGetValue("cmi.location", false);
        
        //if there isn't a stored bookmark, start the user at the first page
        if (bookmark == ""){
            currentPage = 0;
        }
        else{
            //if there is a stored bookmark, prompt the user to resume from the previous location
            if (confirm("Would you like to resume from where you previously left off?")){
                currentPage = parseInt(bookmark, 10);
            }
            else{
                currentPage = 0;
            }
        }
        
        goToPage();
    }
    
    function goToPage(){
    
        var theIframe = document.getElementById("contentFrame");
        var prevButton = document.getElementById("butPrevious");
        var nextButton = document.getElementById("butNext");
        
        //navigate the iFrame to the content
        theIframe.src = "../" + pageArray[currentPage];
        
        //disable the prev/next buttons if we are on the first or last page.
        if (currentPage == 0){
            nextButton.disabled = false;
            prevButton.disabled = true;
        }
        else if (currentPage == (pageArray.length - 1)){
            nextButton.disabled = true;
            prevButton.disabled = false;       
        }
        else{
            nextButton.disabled = false;
            prevButton.disabled = false;
        }
        
        //save the current location as the bookmark
        ScormProcessSetValue("cmi.location", currentPage);
     
        //in this sample course, the course is considered complete when the last page is reached
        if (currentPage == (pageArray.length - 1)){
            reachedEnd = true;
            ScormProcessSetValue("cmi.completion_status", "completed");
        }
    }
    
    function doUnload(pressedExit){
        
        //don't call this function twice
        if (processedUnload == true){return;}
        
        processedUnload = true;
        
        //record the session time
        var endTimeStamp = new Date();
        var totalMilliseconds = (endTimeStamp.getTime() - startTimeStamp.getTime());
        var scormTime = ConvertMilliSecondsIntoSCORM2004Time(totalMilliseconds);
        
        ScormProcessSetValue("cmi.session_time", scormTime);
        
        if (pressedExit == false && reachedEnd == false){
            ScormProcessSetValue("cmi.exit", "suspend");
        }
        
        ScormProcessTerminate();
    }
    
    function doPrevious(){
        if (currentPage > 0){
            currentPage--;
        }
        goToPage();
    }
    
    function doNext(){
        if (currentPage < (pageArray.length - 1)){
            currentPage++;
        }
        goToPage();
    }
    
    function doExit(){

        //note use of short-circuit AND. If the user reached the end, don't prompt.
        //just exit normally and submit the results.
        if (reachedEnd == false && confirm("Would you like to save your progress to resume later?")){
            //set exit to suspend
            ScormProcessSetValue("cmi.exit", "suspend");
            
            //issue a suspendAll navigation request
            ScormProcessSetValue("adl.nav.request", "suspendAll");
        }
        else{
            //set exit to normal
            ScormProcessSetValue("cmi.exit", "");
            
            //issue an exitAll navigation request
            ScormProcessSetValue("adl.nav.request", "exitAll");
        }
        
        //process the unload handler to close out the session.
        //the presense of an adl.nav.request will cause the LMS to 
        //take the content away from the user.
        doUnload(true);
        
    }
    
    //called from the assessmenttemplate.html page to record the results of a test
    //passes in score as a percentage
    function RecordTest(score){
        ScormProcessSetValue("cmi.score.raw", score);
        ScormProcessSetValue("cmi.score.min", "0");
        ScormProcessSetValue("cmi.score.max", "100");
        
        var scaledScore = score / 100;
        ScormProcessSetValue("cmi.score.scaled", scaledScore);
        
        //consider 70% to be passing
        if (score >= 70){
            ScormProcessSetValue("cmi.success_status", "passed");
        }
        else{
            ScormProcessSetValue("cmi.success_status", "failed");
        }
    }
    
    //SCORM requires time to be formatted in a specific way
    function ConvertMilliSecondsIntoSCORM2004Time(intTotalMilliseconds){
	
	    var ScormTime = "";
    	
	    var HundredthsOfASecond;	//decrementing counter - work at the hundreths of a second level because that is all the precision that is required
    	
	    var Seconds;	// 100 hundreths of a seconds
	    var Minutes;	// 60 seconds
	    var Hours;		// 60 minutes
	    var Days;		// 24 hours
	    var Months;		// assumed to be an "average" month (figures a leap year every 4 years) = ((365*4) + 1) / 48 days - 30.4375 days per month
	    var Years;		// assumed to be 12 "average" months
    	
	    var HUNDREDTHS_PER_SECOND = 100;
	    var HUNDREDTHS_PER_MINUTE = HUNDREDTHS_PER_SECOND * 60;
	    var HUNDREDTHS_PER_HOUR   = HUNDREDTHS_PER_MINUTE * 60;
	    var HUNDREDTHS_PER_DAY    = HUNDREDTHS_PER_HOUR * 24;
	    var HUNDREDTHS_PER_MONTH  = HUNDREDTHS_PER_DAY * (((365 * 4) + 1) / 48);
	    var HUNDREDTHS_PER_YEAR   = HUNDREDTHS_PER_MONTH * 12;
    	
	    HundredthsOfASecond = Math.floor(intTotalMilliseconds / 10);
    	
	    Years = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_YEAR);
	    HundredthsOfASecond -= (Years * HUNDREDTHS_PER_YEAR);
    	
	    Months = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_MONTH);
	    HundredthsOfASecond -= (Months * HUNDREDTHS_PER_MONTH);
    	
	    Days = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_DAY);
	    HundredthsOfASecond -= (Days * HUNDREDTHS_PER_DAY);
    	
	    Hours = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_HOUR);
	    HundredthsOfASecond -= (Hours * HUNDREDTHS_PER_HOUR);
    	
	    Minutes = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_MINUTE);
	    HundredthsOfASecond -= (Minutes * HUNDREDTHS_PER_MINUTE);
    	
	    Seconds = Math.floor(HundredthsOfASecond / HUNDREDTHS_PER_SECOND);
	    HundredthsOfASecond -= (Seconds * HUNDREDTHS_PER_SECOND);
    	
	    if (Years > 0) {
		    ScormTime += Years + "Y";
	    }
	    if (Months > 0){
		    ScormTime += Months + "M";
	    }
	    if (Days > 0){
		    ScormTime += Days + "D";
	    }
    	
	    //check to see if we have any time before adding the "T"
	    if ((HundredthsOfASecond + Seconds + Minutes + Hours) > 0 ){
    		
		    ScormTime += "T";
    		
		    if (Hours > 0){
			    ScormTime += Hours + "H";
		    }
    		
		    if (Minutes > 0){
			    ScormTime += Minutes + "M";
		    }
    		
		    if ((HundredthsOfASecond + Seconds) > 0){
			    ScormTime += Seconds;
    			
			    if (HundredthsOfASecond > 0){
				    ScormTime += "." + HundredthsOfASecond;
			    }
    			
			    ScormTime += "S";
		    }
    		
	    }
    	
	    if (ScormTime == ""){
		    ScormTime = "0S";
	    }
    	
	    ScormTime = "P" + ScormTime;
    	
	    return ScormTime;
    }

    </script>

</head>
<body onload="doStart(false);" onbeforeunload="doUnload(false);" onunload="doUnload();">
   
    <iframe width="100%" id="contentFrame" src=""></iframe>
    
    <div id="navDiv">
        <input type="button" value="<- Previous" id="butPrevious" onclick="doPrevious();"/>
        <input type="button" value="Next ->" id="butNext" onclick="doNext();"/>
        <input type="button" value="Exit" id="butExit" onclick="doExit();"/>
    </div>

</body>
</html>
"""
        FileUtils.writeStringToFile(File("$parentPath/launchpage.html"), contentBody, Charset.defaultCharset())
        return "launchpage.html"
    }
}