package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.service.JobReportService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.service.JobTrafficService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobService jobService;

    private CategoryService categoryService;

    @Autowired
    private AESUtil aesUtil;
    @Autowired
    private JobReportService jobReportService;
    @Autowired
    private JobTrafficService jobTrafficService;

    public JobController(JobRepository jobRepository, JobService jobService, CategoryService categoryService, AESUtil aesUtil, JobReportService jobReportService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.categoryService = categoryService;
        this.aesUtil = aesUtil;
        this.jobReportService = jobReportService;
    }

    /*
     * @author: evana
     * @Desc: Job Creation form of a LoggedIn User as Employer
     * @Date: 14-10-25
     * */
    @GetMapping ("/employer/jobs/create_job")
    public String CreateJob(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("job", new Job());
        model.addAttribute("currentPath", "/employer/jobs/create_job");
        return "job_form";
    }

    /*
     * @author: evana
     * @Desc: Job Creation submit of a LoggedIn User as Employer
     * @Date: 14-10-25
     * */
    @PostMapping("/employer/job/submit_job")
    public String job_submit(@ModelAttribute("job") Job job,  @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail, Model model){

        if (loggedUserDetail == null) {
            throw new RuntimeException("No logged-in user");
        }
        log.debug("Job Type: {}", job.getJobType());
        // Get the actual User entity
        User user = loggedUserDetail.getUser();
        String message = "";
        job.setUser(user);
        jobService.JobSave(job);
        message = "Your Job is created successfully. After Admin approval, it will be posted for Freelancer.";
        model.addAttribute("messsage", message);
        model.addAttribute("job", new Job());
        model.addAttribute("jobType", JobType.values());
        return "job_form";
    }

    /*
    * @author: evana
    * @Desc: All job lists of LoggedIn User as Employer
    * @Date: 15-10-25
    * */
    @GetMapping("/employer/jobs/job_list")
    public String allJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findAllJobsWithCategory(loggedUser.getUser());
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/employer/jobs/job_list");
        return "alljob_list";

    }

    /*
     * @author: evana
     * @Desc: Active job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/active_jobs")
    public String activeJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        //log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(), JobStatus.IN_PROGRESS);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);

        model.addAttribute("totalActive", jobs.size());
        model.addAttribute("postedCount", jobs.stream().filter(j -> j.getJobStatus() == JobStatus.POSTED).count());
        model.addAttribute("progressCount", jobs.stream().filter(j -> j.getJobStatus() == JobStatus.IN_PROGRESS).count());
        model.addAttribute("completedCount", jobs.stream().filter(j -> j.getJobStatus() == JobStatus.COMPLETED).count());

        model.addAttribute("currentPath", "/employer/jobs/active_job");

        return "active_jobs";
    }

    /*
     * @author: evana
     * @Desc: Posted job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/posted_jobs")
    public String postedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(),JobStatus.POSTED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/employer/jobs/posted_job");
        return "posted_jobs";
    }

    /*
     * @author: evana
     * @Desc: Pending job lists of for admin to approve
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/pending_jobs")
    public String pendingForAdminJobList(Model model) {
        List<Job> jobs = jobService.findByJobStatusForAdmin(JobStatus.PENDING);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/admin/jobs/pending_job");

        return "admin_pending_jobs";
    }

    /*
     * @author: evana
     * @Desc: Pending job lists of for admin to approve
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/posted_jobs")
    public String postedForAdminJobList(Model model) {
        List<Job> jobs = jobService.findByJobStatusForAdmin(JobStatus.POSTED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/admin/jobs/posted_job");

        return "admin_posted_jobs";
    }

    /*
     * @author: evana
     * @Desc: Canceled job lists of for admin to approve
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/canceled_jobs")
    public String canceledForAdminJobList(Model model) {
        List<Job> jobs = jobService.findByJobStatusForAdmin(JobStatus.CANCELLED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/admin/jobs/canceled_job");
        return "admin_canceled_jobs";
    }

    /*
     * @author: evana
     * @Desc: Active job lists of for admin to approve
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/active_jobs")
    public String activeForAdminJobList(Model model) {
        List<Job> jobs = jobService.findByJobStatusForAdmin(JobStatus.IN_PROGRESS);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/admin/jobs/active_job");

        return "admin_active_jobs";
    }

    /*
     * @author: evana
     * @Desc: Completed job lists of for admin
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/completed_jobs")
    public String completeForAdminJobList(Model model) {
        List<Job> jobs = jobService.findByJobStatusForAdmin(JobStatus.COMPLETED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/admin/jobs/completed_job");

        return "admin_complete_jobs";
    }

    /*
     * @author: evana
     * @Desc: Completed job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */

    @GetMapping("/employer/jobs/completed_jobs")
    public String completeJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(), JobStatus.COMPLETED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/employer/jobs/completed_job");

        return "completed_jobs";
    }

    /*
     * @author: evana
     * @Desc: Pending job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/pending_jobs")
    public String pendingJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(), JobStatus.PENDING);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/employer/jobs/pending_job");
        return "pending_jobs";
    }

    /*
     * @author: evana
     * @Desc: Closed job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/closed_jobs")
    public String closeJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(), JobStatus.CANCELLED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("currentPath", "/employer/jobs/closed_job");
        return "closed_jobs";
    }

    /*
     * @author: evana
     * @Desc: Completed job lists for payment pending of LoggedIn User as Employer
     * @Date: 18-12-26
     * */

    @GetMapping("/employer/payments/pending_payments")
    public String pendingPaymentJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());

        List<JobTraffic> jobPayments= jobTrafficService.findJobTrafficByStatus(loggedUser.getId(), TrafficType.SUBMITTED);

        model.addAttribute("aesUtil", aesUtil);

        model.addAttribute("jobPayments", jobPayments);
        model.addAttribute("pendingCount", jobPayments.size());
        model.addAttribute("pendingAmount", jobPayments.size());

        model.addAttribute("currentPath", "/employer/payments/pending");

        return "employee_pending_payments";
    }

    /*
     * @author: evana
     * @Desc: New job lists of LoggedIn User as Employee
     * @Date: 28-10-25
     * */

    @GetMapping("/employee/jobs/new_jobs")
    public String newJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findByNewJobStatus(JobStatus.POSTED);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "employee_new_jobs";
    }

    /*
     * @author: evana
     * @Desc: Recommended jobs based on user skills of LoggedIn User as Employee
     * @Date: 28-10-25
     * */

    @GetMapping("/employee/jobs/recommended_jobs")
    public String recommendedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<Job> jobs = jobService.findByRecommendedJobStatus(JobStatus.POSTED, userId);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "recommended_jobs";
    }

    /*
     * @author: evana
     * @Desc: Saved jobs based on user skills of LoggedIn User as Employee
     * @Date: 30-10-25
     * */

    @GetMapping("/employee/jobs/saved_jobs")
    public String savedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<Job> jobs = jobService.findBySavedJobs(TrafficType.SAVED, userId);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "saved_jobs";
    }

    /*
     * @author: evana
     * @Desc: Applied jobs of LoggedIn User as Employee
     * @Date: 31-10-25
     * */
    @GetMapping("/employee/jobs/applied_jobs")
    public String appliedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<Job> jobs = jobService.findBySavedJobs(TrafficType.APPLIED, userId);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "applied_jobs";
    }

    /*
     * @author: evana
     * @Desc: InProgress jobs of LoggedIn User as Employee
     * @Date: 31-10-25
     * */
    @GetMapping("/employee/jobs/working_jobs")
    public String workingJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<Job> jobs = jobService.findBySavedJobs(TrafficType.WORKING, userId);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "working_jobs";
    }

    /*
     * @author: evana
     * @Desc: Completed jobs of LoggedIn User as Employee
     * @Date: 31-10-25
     * */
    @GetMapping("/employee/jobs/completed_jobs")
    public String completedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<Job> jobs = jobService.findBySavedJobs(TrafficType.SUBMITTED, userId);
        model.addAttribute("jobs", jobs);
        model.addAttribute("aesUtil", aesUtil);
        return "employee_completed_jobs";
    }

    /*
     * @author: evana
     * @Desc: Reported jobs of LoggedIn User as Employee
     * @Date: 31-10-25
     * */
    @GetMapping("/employee/jobs/reported_jobs")
    public String reportedJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        List<JobReport> reports = jobReportService.findByReportJob(userId);
        model.addAttribute("jobReports", reports);
        model.addAttribute("aesUtil", aesUtil);
        return "reported_jobs";
    }


    /*
     * @author: evana
     * @Desc: Job Details of a specific job
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/job_detail")
    public String jobDetail(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Job job  = jobService.findById(did);
            log.debug("Job details: {}", job);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("jobDetail", job);
            model.addAttribute("jobStatus", JobStatus.values());
            model.addAttribute("jobType", JobType.values());
            model.addAttribute("categories", categories);
        }else{
            model.addAttribute("message", "Job not available.");
        }
        return "fragments/job_detail :: jobDetail";
    }

    /*
     * @author: evana
     * @Desc: Job Details of a specific job for Admin
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/job_detail")
    public String adminJobDetail(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Job job  = jobService.findById(did);
            log.debug("Job details: {}", job);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("jobDetail", job);
            model.addAttribute("jobStatus", JobStatus.values());
            model.addAttribute("jobType", JobType.values());
            model.addAttribute("categories", categories);
            model.addAttribute("currentPath", "/admin/jobs/job_detail");

        }else{
            model.addAttribute("message", "Job not available.");
        }

        return "fragments/job_detail :: jobDetail";
    }

    /*
     * @author: evana
     * @Desc: Job Update of a specific job
     * @Date: 15-10-25
     * */
    @PostMapping("/employer/jobs/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> JobStatusUpdate(@ModelAttribute("jobDetail") Job job,
   @RequestParam JobStatus jobStatus, @RequestParam JobType jobType)
    {
        Map<String, String> response = new HashMap<>();

        log.info("Job status: {}", jobStatus);
        log.info("Job Type: {}", jobType);
        Optional<Job> existingjob = jobRepository.findById(job.getId());
            if (existingjob.isPresent()){
                //job.setJobStatus(jobStatus);
                //job.setJobType(jobType);
                //log.info("After set Job status: {}", job.getJobStatus());
                //log.info("After Set Job Type: {}", job.getJobType());
                jobService.update_job(job, jobStatus, jobType);
                response.put("status", "success");
                return ResponseEntity.ok(response);
            }

        response.put("status", "failed");
        return ResponseEntity.ok(response);
    }

    /*
     * @author: evana
     * @Desc: Job Status Update of a specific job by Admin
     * @Date: 26-10-25
     * */
    @GetMapping("/admin/jobs/job_update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> JobStatusUpdateByAdmin(@RequestParam("encId") String encId) {
        Map<String, String> response = new HashMap<>();
        Long did = aesUtil.decryptId(encId);
        Optional<Job> existingjob = jobRepository.findById(did);
        if (existingjob.isPresent()){
            log.info("JobId: {}", did);
            jobService.updateJobStatus(did, JobStatus.POSTED);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        response.put("status", "failed");
        return ResponseEntity.ok(response);
    }

    /*
     * @author: evana
     * @Desc: Job list of a specific category
     * @Date: 16-10-25
     * */
    @GetMapping("/jobs/category/{encId}")
    public String getJobsByCategory(@PathVariable("encId") String encId, Model model) {
        try {
            Long did = aesUtil.decryptId(encId);
            Category category = categoryService.findById(did);
            if(category.getId() != null) {
                List<Job> jobs = jobService.findByCategoryAndJobStatus(category);
                model.addAttribute("category", category);
                model.addAttribute("jobs", jobs);
                model.addAttribute("currentPath", "/jobs/category/{category.name}");

                return "category_jobs";
            }
            else{
                model.addAttribute("message","Category not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, redirect to an error page or show a message
            return "redirect:/error";
        }
        return "category_jobs";
    }

    @GetMapping("/jobs/job_detail/{encId}")
    public String getJobDetail(@PathVariable("encId") String encId, @ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        try {
            Long did = aesUtil.decryptId(encId);
            Job job = jobService.findById(did);
            if (job == null) {
                return "redirect:jobs_not_found";
            }
            List<JobTraffic> jobTraffics = jobTrafficService.findJobTrafficByUserIdJobId(loggedUser.getId(), did);
            log.info("Jobtraffics: {}", jobTraffics.toString());
            if(jobTraffics.isEmpty()){
                model.addAttribute("jobTraffics",null);
            }
            else{
                for (JobTraffic traffic : jobTraffics) {
                    log.info("traffic: {}", traffic.getTrafficType());
                    if (traffic.getTrafficType() == TrafficType.SAVED) {
                        model.addAttribute("saveStatus", "Saved");
                    } else if (traffic.getTrafficType() == TrafficType.REPORT) {
                        model.addAttribute("reportStatus", "Reported");
                    }else if (traffic.getTrafficType() == TrafficType.APPLIED) {
                        model.addAttribute("applyStatus", "Applied");
                    }else if (traffic.getTrafficType() == TrafficType.SUBMITTED) {
                        model.addAttribute("submitStatus", "Submitted");
                    }
                    else if (traffic.getTrafficType() == TrafficType.REJECTED) {
                        model.addAttribute("rejectStatus", "Rejected");
                    }
                }

            }
            model.addAttribute("jobDetail", job);
        return "categoryJob_detail";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:error";
        }
    }

}
