package com.acubeapps.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.acubeapps.service.pojo.CompleteCourseRequest;
import com.acubeapps.service.pojo.CompleteCourseResponse;
import com.acubeapps.service.pojo.CourseDetails;
import com.acubeapps.service.pojo.Error;
import com.acubeapps.service.pojo.GetCourseDetailsRequest;
import com.acubeapps.service.pojo.GetCourseDetailsResponse;
import com.acubeapps.service.pojo.McqOptions;
import com.acubeapps.service.pojo.Question;
import com.acubeapps.storage.dynamodb.dao.ChildCourseDao;
import com.acubeapps.storage.dynamodb.dao.ChildLoginDetailsDao;
import com.acubeapps.storage.dynamodb.dao.CourseDetailsDao;
import com.acubeapps.storage.dynamodb.dao.ParentLoginDetailsDao;
import com.acubeapps.storage.dynamodb.pojo.ChildCourseDetails;
import com.acubeapps.storage.dynamodb.pojo.CourseDetailsDdb;
import com.acubeapps.utils.GcmNotificationSender;

@Path("/courses")
public class CourseService {

    private final CourseDetailsDao courseDao;
    private final ChildCourseDao childCourseDao;
    private final ChildLoginDetailsDao childLoginDao;
    private final ParentLoginDetailsDao parentLoginDao;

    public CourseService() {
        courseDao = new CourseDetailsDao();
        childCourseDao = new ChildCourseDao();
        childLoginDao = new ChildLoginDetailsDao();
        parentLoginDao = new ParentLoginDetailsDao();
    }

    @Path("/get")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GetCourseDetailsResponse getCourseDetails(GetCourseDetailsRequest request) {
        GetCourseDetailsResponse response = new GetCourseDetailsResponse();
        CourseDetailsDdb details = courseDao.get(request.getCourseId());

        if (details == null) {
            response.setStatus("failure");
            Error error = new Error();
            error.setCode(404);
            error.setMessage("no policy set for child");
            List<Error> errorList = new ArrayList<>();
            errorList.add(error);
            response.setErrorList(errorList);
        } else {
            response.setStatus("success");
            response.setCourseDetails(details.getCourseDetails());
        }
        return response;
    }

    @Path("/getAll")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<CourseDetailsDdb> getAllCourses() {
        return courseDao.getAll();
    }

    @Path("/reportComplete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompleteCourseResponse completeCourse(CompleteCourseRequest request) {
        CompleteCourseResponse response = new CompleteCourseResponse();
        ChildCourseDetails details = childCourseDao.get(request.getChildId(), request.getCourseId());
        if (details == null) {
            response.setStatus("failure");
            Error error = new Error();
            error.setCode(404);
            error.setMessage("entry not found");
            List<Error> errorList = new ArrayList<>();
            errorList.add(error);
            response.setErrorList(errorList);
        } else {
            details.setCompletionStatus("COMPLETED");
            details.setQuestionList(request.getQuestionList());
            //TODO: compute actual percentile
            details.setPercentile("85");
            childCourseDao.save(details);

            String parentId = childLoginDao.getByChildId(request.getChildId()).getParentUserId();
            JSONObject msg = new JSONObject();
            msg.put("action", "courseCompleted");
            msg.put("course", details);
            GcmNotificationSender.sendGcm(parentLoginDao.getByParentId(parentId).getGcmToken(), msg);
            response.setStatus("success");
        }
        return response;
    }

    @Path("/addCourse")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public CompleteCourseResponse addCourse() {
        CompleteCourseResponse response = new CompleteCourseResponse();
        response.setStatus("success");

        Question q1 = new Question();
        q1.setQuestionId("qid1");
        q1.setQuestionText("What is 2 + 5");
        q1.setQuestionType("mcq");

        List<McqOptions> options = new ArrayList<>();
        McqOptions option1 = new McqOptions();
        option1.setOptionSequence("1");
        option1.setOptionText("5");

        McqOptions option2 = new McqOptions();
        option2.setOptionSequence("2");
        option2.setOptionText("6");

        McqOptions option3 = new McqOptions();
        option3.setOptionSequence("3");
        option3.setOptionText("7");

        McqOptions option4 = new McqOptions();
        option4.setOptionSequence("4");
        option3.setOptionText("8");

        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        q1.setOptions(options);
        q1.setSolution("3");
        List<Question> questionList = new ArrayList<>();
        questionList.add(q1);

        Question q2 = new Question();
        q2.setQuestionId("qid2");
        q2.setQuestionText("Take a photo of something GREEN!!");
        q2.setQuestionType("subjective");

        questionList.add(q2);
        CourseDetails details = new CourseDetails();
        details.setQuestionList(questionList);

        CourseDetailsDdb courseDetailsDdb = new CourseDetailsDdb();
        courseDetailsDdb.setCourseId(UUID.randomUUID().toString());
        courseDetailsDdb.setCourseDetails(details);
        courseDao.save(courseDetailsDdb);
        return response;
    }
}
