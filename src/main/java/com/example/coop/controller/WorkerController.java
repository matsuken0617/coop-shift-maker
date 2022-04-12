package com.example.coop.controller;

import java.text.ParseException;
import java.util.List;

import com.example.coop.dto.WorkShiftListForm;
import com.example.coop.entity.Worker;
import com.example.coop.service.WorkShiftService;
import com.example.coop.service.WorkerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    @Autowired
    WorkerService wService;
    @Autowired
    WorkShiftService wsService;

    /**
     * ログイン画面
     * 
     * @param model
     * @return
     */
    @GetMapping("")
    String showLoginPage(Model model) {
        return "worker/login";
    }

    /**
     * ログイン処理
     * 
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/login")
    String login(@RequestParam Long id, Model model) {
        wService.getWorker(id);
        return "redirect:/worker/" + id;
    }

    /**
     * TOP画面
     * 
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id}")
    String showTopPage(@PathVariable Long id, Model model) {
        // シフトカレンダー
        List<String> dates = wsService.strDateList();
        model.addAttribute("dates", dates);
        System.out.println(dates);
        int[][] shiftCalendar = wsService.makeShiftCalender();
        model.addAttribute("shiftCalendar", shiftCalendar);
        // ワーカー一覧
        List<Worker> workers = wService.getAllWorkers();
        model.addAttribute("workers", workers);
        // ユーザ
        Worker worker = wService.getWorker(id);
        model.addAttribute("worker", worker);
        return "worker/index";
    }

    /**
     * シフト提出画面
     * 
     * @param id
     * @param model
     * @return
     * @throws ParseException
     */
    @GetMapping("/{id}/work-shift/create")
    String showWorkShiftCreatePage(@PathVariable Long id, Model model) throws ParseException {
        Worker worker = wService.getWorker(id);
        model.addAttribute("worker", worker);
        model.addAttribute("forms", wsService.createWorkShiftListForm(id));
        return "worker/work_shift_create";
    }

    /**
     * シフト提出確認画面
     * 
     * @param id
     * @param forms
     * @param model
     * @return
     */
    @PostMapping("/{id}/work-shift/create/check")
    String workShiftCreateCheck(@PathVariable Long id, @ModelAttribute(name = "forms") WorkShiftListForm forms,
            Model model) {
        Worker worker = wService.getWorker(id);
        model.addAttribute("worker", worker);
        model.addAttribute("forms", forms);
        return "worker/work_shift_create_check";
    }

    /**
     * シフトを提出する
     * @param id
     * @param forms
     * @param model
     * @return
     * @throws ParseException
     */
    @PostMapping("/{id}/work-shift/create")
    String createWorkShift(@PathVariable Long id, @ModelAttribute(name = "forms") WorkShiftListForm forms,
            Model model) throws ParseException {
        wsService.createWorkShiftByFormList(forms);
        return "redirect:/worker/" + id;
    }
}
