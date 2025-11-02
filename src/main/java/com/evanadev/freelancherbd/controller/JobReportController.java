package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobReportRepository;
import com.evanadev.freelancherbd.service.JobReportService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.service.JobTrafficService;
import com.evanadev.freelancherbd.service.UserService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/freelancer/jobs")
public class JobReportController {
    private final JobReportRepository jobReportRepository;
    private final JobService jobService;
    private final UserService userService;
    private final JobReportService jobReportService;
    private final JobTrafficService  jobTrafficService;
    private AESUtil aesUtil;

    public JobReportController(JobReportRepository jobReportRepository, JobService jobService, UserService userService, JobReportService jobReportService, JobTrafficService jobTrafficService, AESUtil aesUtil) {
        this.jobReportRepository = jobReportRepository;
        this.jobService = jobService;
        this.userService = userService;
        this.jobReportService = jobReportService;
        this.jobTrafficService = jobTrafficService;
        this.aesUtil = aesUtil;
    }

    @GetMapping("/{id}/report")
    public String showReportForm(@PathVariable String id, Model model) {

       // Long did = aesUtil.decryptId(id);
        model.addAttribute("jobId", id);
        model.addAttribute("reasons", ReportReason.values());
        model.addAttribute("application_message","");
        return "report_job";
    }

    @PostMapping("/{id}/report")
    public String submitReport(@PathVariable String id,
                               @RequestParam ReportReason reason,
                               @RequestParam(required = true) String description,
                               @ModelAttribute("loggedUser") CustomUserDetail loggedUser) {

        Long did = aesUtil.decryptId(id);
        String userEmail = loggedUser.getUser().getEmail();
        Job job = jobService.findById(did);

        JobReport report = new JobReport();
        report.setJob(job);
        report.setUser(loggedUser.getUser());
        report.setReportReason(reason);
        report.setDescription(description);
        jobReportService.save(report);

        JobTraffic jobTraffic = new JobTraffic();
        jobTraffic.setJob(job);
        jobTraffic.setUser(loggedUser.getUser());
        jobTraffic.setTrafficType(TrafficType.REPORT);
        jobTrafficService.save(jobTraffic);

        //return "redirect:/freelancer/jobs/" + id + "/report";
        return "jobReport_submit";
    }


}
