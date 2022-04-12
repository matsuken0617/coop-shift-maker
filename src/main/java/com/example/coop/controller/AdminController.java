package com.example.coop.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import com.example.coop.dto.WorkerForm;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    WorkerService wService;
    @Autowired
    WorkShiftService wsService;

    /**
     * TOPページ
     * 
     * @param model
     * @return
     */
    @GetMapping("")
    String showTopPage(Model model) {
        // シフトカレンダー
        List<String> dates = wsService.strDateList();
        model.addAttribute("dates", dates);
        System.out.println(dates);
        int[][] shiftCalendar = wsService.makeShiftCalender();
        model.addAttribute("shiftCalendar", shiftCalendar);
        // ワーカー一覧
        List<Worker> workers = wService.getAllWorkers();
        model.addAttribute("workers", workers);
        return "admin/index";
    }

    /**
     * メンバー管理画面
     * 
     * @param model
     * @return
     */
    @GetMapping("/worker")
    String showWorkerPage(Model model) {
        WorkerForm form = new WorkerForm();
        model.addAttribute("form", form);
        List<Worker> workers = wService.getAllWorkers();
        model.addAttribute("workers", workers);
        return "admin/worker";
    }

    /**
     * メンバー作成
     * 
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/worker/create")
    String createWorker(@ModelAttribute(name = "form") WorkerForm form, Model model) {
        wService.createWorker(form);
        return "redirect:/admin/worker";
    }

    /**
     * メンバー削除確認画面
     * 
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/worker/delete/{id}")
    String showWorkerDeleteCheck(@PathVariable Long id, Model model) {
        Worker w = wService.getWorker(id);
        model.addAttribute("worker", w);
        return "admin/worker_delete";
    }

    /**
     * メンバー削除
     * 
     * @param id
     * @param model
     * @return
     */
    @PostMapping("worker/delete/{id}")
    String deleteWorker(@PathVariable Long id, Model model) {
        wService.deleteWorker(id);
        return "redirect:/admin/worker";
    }

    /**
     * シフト作成の確認
     * @param model
     * @return
     */
    @GetMapping("shift/create/check")
    String createShiftCheck(Model model) {
        // シフトカレンダー
        List<String> dates = wsService.strDateList();
        model.addAttribute("dates", dates);
        System.out.println(dates);
        int[][] shiftCalendar = wsService.makeShiftCalender();
        model.addAttribute("shiftCalendar", shiftCalendar);
        // ワーカー一覧
        List<Worker> workers = wService.getAllWorkers();
        model.addAttribute("workers", workers);
        return "admin/shift_create";
    }

    /**
     * シフト作成
     * @param model
     * @return
     * @throws ParseException
     */
    @GetMapping("shift/create")
    String createShift(Model model) throws ParseException {
        // シフト作成
        /// 今の日付
        LocalDate dt1 = LocalDate.of(2022, 4, 15);
        LocalDate dt2 = LocalDate.of(2022, 5, 15);
        wsService.updateShiftCalender(dt1, dt2);
        return "redirect:/admin";
    }
}
