package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.service.JobService;
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

    public JobController(JobRepository jobRepository, JobService jobService, CategoryService categoryService, AESUtil aesUtil) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.categoryService = categoryService;
        this.aesUtil = aesUtil;
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
        return "job_form";
    }

    /*
     * @author: evana
     * @Desc: Job Creation submit of a LoggedIn User as Employer
     * @Date: 14-10-25
     * */
    @PostMapping("/employer/job/submit_job")
    public String job_submit(@ModelAttribute Job job,  @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail, Model model){

        if (loggedUserDetail == null) {
            throw new RuntimeException("No logged-in user");
        }
        // Get the actual User entity
        User user = loggedUserDetail.getUser();
        String message = "";
        job.setUser(user);
        jobService.JobSave(job);
        message = "Your Job is created successfully. After Admin approval, it will be posted for Freelancer.";
        model.addAttribute("messsage", message);
        model.addAttribute("jobType", JobType.values());
        model.addAttribute("job", new Job());
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
        return "alljob_list";

    }

    /*
     * @author: evana
     * @Desc: Active job lists of LoggedIn User as Employer
     * @Date: 15-10-25
     * */
    @GetMapping("/employer/jobs/active_jobs")
    public String activeJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
       // String statusStr = "IN_PROGRESS";
       // JobStatus status = JobStatus.valueOf(statusStr);
        List<Job> jobs = jobService.findByJobStatus(loggedUser.getUser(), JobStatus.IN_PROGRESS);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
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
        return "posted_jobs";
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
        return "closed_jobs";
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
     * @Desc: Job Update of a specific job
     * @Date: 15-10-25
     * */
    @PostMapping("/employer/jobs/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> UserStatusUpdate(@ModelAttribute Job job)
    {
        Map<String, String> response = new HashMap<>();

        Optional<Job> existingjob = jobRepository.findById(job.getId());
            if (existingjob.isPresent()){
                jobService.update_job(job);
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
    public String getJobDetail(@PathVariable("encId") String encId, Model model) {
        try {
            Long did = aesUtil.decryptId(encId);
            Job job = jobService.findById(did);
            if (job == null) {
                return "redirect:jobs_not_found";
            }
            model.addAttribute("jobDetail", job);
            return "categoryJob_detail";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:error";
        }
    }

}
