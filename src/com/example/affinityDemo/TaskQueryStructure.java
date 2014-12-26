package com.example.affinityDemo;

/**
 * Created by Gareth on 22/12/2014.
 */
public class TaskQueryStructure {

    private static String baseTaskURL;
    private static String filterDate;
    private static String taskStatus;
    private static String assigneeFilter;
    private static String tokenParameter;
    private static String baseCompleteURL;
    private static String progressionData;
    private static String specificPage;


    TaskQueryStructure()
    {
        baseTaskURL = "https://demo.api.staging.affinitylive.com/api/v0/tasks?";
        filterDate = "_filters=date_due_before(" + System.currentTimeMillis() / 1000L + "),";
        taskStatus = "standing(pending,accepted,started)&";
        assigneeFilter = "_fields=assignee(email)&";
        tokenParameter = "_bearer_token=";

        baseCompleteURL = "https://demo.api.staging.affinitylive.com/api/v0/tasks/";
        progressionData = "/progressions/done?";

        specificPage = "_page=";



    }

    String generateTaskURL(String token, int pageNumber)
    {
        String result = "";
        result = baseTaskURL + filterDate + taskStatus + assigneeFilter + tokenParameter + token +"&" + specificPage + pageNumber;

        return result;
    }

    String generateTaskCompletion(String token, String taskID)
    {
        String result = "";
        result = baseCompleteURL + taskID + progressionData +tokenParameter + token;

        return result;
    }

}
