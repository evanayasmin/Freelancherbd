package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.NotificationDTO;
import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobReportRepository;
import com.evanadev.freelancherbd.service.*;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/freelancer/jobs")
public class JobReportController {
    private final JobReportRepository jobReportRepository;
    private final JobService jobService;
    private final UserService userService;
    private final JobReportService jobReportService;
    private final JobTrafficService  jobTrafficService;
    private final NotificationService notificationService;
    private AESUtil aesUtil;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public JobReportController(JobReportRepository jobReportRepository, JobService jobService, UserService userService, JobReportService jobReportService, JobTrafficService jobTrafficService, NotificationService notificationService, AESUtil aesUtil) {
        this.jobReportRepository = jobReportRepository;
        this.jobService = jobService;
        this.userService = userService;
        this.jobReportService = jobReportService;
        this.jobTrafficService = jobTrafficService;
        this.notificationService = notificationService;
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
        String userEmail = loggedUser.getUser().getUsername();
        Job job = jobService.findById(did);
        Long jobId = job.getId();
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

        Optional<User> adminUser = userService.findAdminUser();
        if(adminUser.isPresent()){
            Notification notification = new Notification();
            notification.setTitle("Job Reported");
            notification.setMessage("A job with Title: " + job.getTitle() + " has been reported. Reason: " + reason);
            notification.setType("REPORT");
            notification.setSender(loggedUser.getUser());
            notification.setRecipient(adminUser.get());
            notificationService.sendNotification(notification, adminUser.get());
        }
        //return "redirect:/freelancer/jobs/" + id + "/report";
        return "jobReport_submit";
    }

    @GetMapping("/test-notification")
    public String testNotification() {
        NotificationDTO dto = new NotificationDTO(
                "Test Notification",
                "Hello Admin! This is a test.",
                "TEST",
                "System",
                1L,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/admin", dto);
        return "Sent";
    }


}
