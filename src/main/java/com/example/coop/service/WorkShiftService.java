package com.example.coop.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.coop.dto.WorkShiftForm;
import com.example.coop.dto.WorkShiftListForm;
import com.example.coop.entity.WorkShift;
import com.example.coop.entity.Worker;
import com.example.coop.exception.CoopException;
import com.example.coop.repository.WorkShiftRepository;
import com.example.coop.repository.WorkerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkShiftService {
    @Autowired
    WorkShiftRepository wsRepo;
    @Autowired
    WorkerRepository wRepo;

    /**
     * 作成
     * 
     * @param form
     * @return
     * @throws ParseException
     */
    public WorkShift createWorkShift(WorkShiftForm form) throws ParseException {
        WorkShift ws = form.toEntity();
        System.out.println(ws);
        return wsRepo.save(ws);
    }

    /**
     * 取得
     * 
     * @param id
     * @return
     */
    public WorkShift getWorkShift(Long id) {
        WorkShift ws = wsRepo.findById(id).orElseThrow(
                () -> new CoopException(CoopException.NO_SUCH_WORK_SHIFT_EXISTS, "No exists"));
        return ws;
    }

    /**
     * 全件取得
     * 
     * @return
     */
    public List<WorkShift> getAllWorkShifts() {
        return wsRepo.findAll();
    }

    /**
     * 削除
     * 
     * @param id
     */
    public void deleteWorkShift(Long id) {
        WorkShift ws = getWorkShift(id);
        wsRepo.delete(ws);
    }

    /**
     * シフト登録フォームを作成する
     * 
     * @param id
     * @return
     */
    public WorkShiftListForm createWorkShiftListForm(Long id) {
        // シフト登録フォームの作成
        WorkShiftListForm forms = new WorkShiftListForm();
        // 日付の取得
        LocalDate today = LocalDate.now();
        LocalDate nextMonth;
        if (today.getDayOfMonth() < 15) {
            nextMonth = today;
        } else {
            nextMonth = today.plusMonths(1);
        }
        LocalDate startDt = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 15);
        LocalDate endDt = LocalDate.of(nextMonth.plusMonths(1).getYear(), nextMonth.plusMonths(1).getMonth(),
                15);
        // フォーム作成部分
        LocalDate ld = startDt;
        while (ld.isBefore(endDt)) {
            WorkShiftForm wsForm = new WorkShiftForm();
            wsForm.setWorkerId(id);
            wsForm.setDate(ld.format(DateTimeFormatter.ofPattern("yyyy/MM/dd E")));
            forms.add(wsForm);
            ld = ld.plusDays(1);
        }
        return forms;
    }

    /**
     * シフト提出フォームからシフトを作成
     * 
     * @param forms
     * @return
     * @throws ParseException
     */
    public boolean createWorkShiftByFormList(WorkShiftListForm forms) throws ParseException {
        for (WorkShiftForm form : forms.getList()) {
            // formからシフトの日付を取得
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd E");
            LocalDate dt = LocalDate.parse(form.getDate(), formatter);
            // すでにシフトがある場合，上書き
            System.out.println(form);
            List<WorkShift> wsList = wsRepo.findByWorkerIdAndDate(form.getWorkerId(), dt);
            for (WorkShift ws : wsList) {
                wsRepo.delete(ws);
            }
            createWorkShift(form);
        }
        return true;
    }

    /**
     * シフトカレンダーを作成
     * 
     * @return
     */
    public int[][] makeShiftCalender() {
        // 日付を決める
        LocalDate today = LocalDate.now();
        LocalDate startDt;
        LocalDate endDt;
        if (today.getDayOfMonth() < 15) {
            startDt = LocalDate.of(today.getYear(), today.getMonth(), 15);
            endDt = LocalDate.of(today.plusMonths(1).getYear(), today.plusMonths(1).getMonth(), 15);
        } else {
            startDt = LocalDate.of(today.plusMonths(1).getYear(), today.plusMonths(1).getMonth(), 15);
            endDt = LocalDate.of(today.plusMonths(2).getYear(), today.plusMonths(2).getMonth(), 15);
        }
        // ワーカー全員取得
        List<Worker> workers = wRepo.findAll();
        // カレンダー作成
        int[][] workCalendar = new int[workers.size()][(int) ChronoUnit.DAYS.between(startDt, endDt)];
        for (int i = 0; i < workers.size(); i++) {
            List<WorkShift> workShifts = wsRepo.findByWorkerIdAndDateBetween(workers.get(i).getId(), startDt,
                    endDt);
            for (WorkShift ws : workShifts) {
                workCalendar[i][(int) ChronoUnit.DAYS.between(startDt, ws.getDate())] = ws.getWork();
            }
        }
        return workCalendar;
    }

    /**
     * カレンダー表示用の文字列リスト。
     * 
     * @return
     */
    public List<String> strDateList() {
        // 日付を決める
        LocalDate today = LocalDate.now();
        LocalDate startDt;
        LocalDate endDt;
        if (today.getDayOfMonth() < 15) {
            startDt = LocalDate.of(today.getYear(), today.getMonth(), 15);
            endDt = LocalDate.of(today.plusMonths(1).getYear(), today.plusMonths(1).getMonth(), 15);
        } else {
            startDt = LocalDate.of(today.plusMonths(1).getYear(), today.plusMonths(1).getMonth(), 15);
            endDt = LocalDate.of(today.plusMonths(2).getYear(), today.plusMonths(2).getMonth(), 15);
        }
        LocalDate dt = startDt;
        List<String> list = new ArrayList<>();
        while (dt.isBefore(endDt)) {
            if (dt.getDayOfMonth() == 1 || dt.getDayOfMonth() == 15) {
                list.add(dt.getMonthValue() + "/" + dt.getDayOfMonth());
            } else {
                list.add(dt.getDayOfMonth() + "");
            }
            dt = dt.plusDays(1);
        }
        return list;
    }

    /**
     * 日付を指定し，シフトを作成する（〇をつける）
     * 
     * @throws ParseException
     */
    public void updateShiftCalender(LocalDate startDt, LocalDate endDt) throws ParseException {
        // 期間中の〇シフトを削除
        deleteWorkShiftByDateBetween(startDt, endDt);
        // Worker全員を取得
        List<Worker> workers;
        // Worker全員の疲労度マップを準備
        Map<Long, Integer> fatigueMap = new HashMap<>();
        LocalDate dt = startDt;
        LocalDate dt2;
        // 何日前までシフトを見るか
        int rangeDate = 7;
        // 曜日情報，各曜日に何人入るか
        final Map<Integer, Integer> DAY_OF_WEEK_SHIFT_MAP = new HashMap<>() {
            {
                put(1, 2); // 月曜日，2人
                put(2, 2); // 火曜日，2人
                put(3, 2); // 水曜日，2人
                put(4, 3); // 木曜日，3人
                put(5, 3); // 金曜日，3人
                put(6, 2); // 土曜日，2人
                put(7, 3); // 日曜日，3人
            }
        };
        // 〇をつけていく
        while (dt.isBefore(endDt)) {
            // workerの再取得
            workers = wRepo.findAll();
            // 全員の疲労度を初期化
            fatigueMap.clear();
            // 当日のシフト状況を取得
            List<WorkShift> shifts = wsRepo.findByDate(dt);
            // 当日に×がついているworkerを削除
            for (WorkShift ws : shifts) {
                if (ws.getWork() < 0) {
                    // workers探索して削除
                    for (int i = 0; i < workers.size(); i++) {
                        if (workers.get(i).getId() == ws.getWorkerId()) {
                            workers.remove(i);
                            break;
                        }
                    }
                }
            }
            // 疲労度マップを作成
            for (Worker w : workers) {
                fatigueMap.put(w.getId(), 0);
            }
            // 1日前から順にシフトを確認
            for (int i = 1; i <= rangeDate; i++) {
                dt2 = dt.minusDays(i);
                // シフトを取得
                List<WorkShift> shifts2 = wsRepo.findByDate(dt2);
                // 疲労度を加算
                for (WorkShift ws : shifts2) {
                    // 〇があれば疲労度を加算
                    if (ws.getWork() > 0) {
                        // workerがマップ計算対象外か確認
                        if (!fatigueMap.containsKey(ws.getWorkerId()))
                            continue;
                        int fatigue = fatigueMap.get(ws.getWorkerId());
                        fatigueMap.put(ws.getWorkerId(), fatigue + (rangeDate - i + 1));
                    }
                }
            }
            // マップを疲労度順にソート
            /// Map.Entryのリストを作成
            List<Entry<Long, Integer>> list_entries = new ArrayList<Entry<Long, Integer>>(fatigueMap.entrySet());
            /// 比較関数Comparaterを使用してMap.Entryの値を比較
            Collections.sort(list_entries, new Comparator<Entry<Long, Integer>>() {
                public int compare(Entry<Long, Integer> obj1, Entry<Long, Integer> obj2) {
                    // 4. 昇順
                    return obj1.getValue().compareTo(obj2.getValue());
                }
            });
            // 疲労度の少ない順に，シフトを作成
            int count = 0;
            for (Entry<Long, Integer> entry : list_entries) {
                WorkShiftForm form = new WorkShiftForm(null, entry.getKey(),
                        dt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd E")), 1);
                createWorkShift(form);
                count++;
                if (count >= DAY_OF_WEEK_SHIFT_MAP.get(dt.getDayOfWeek().getValue())) {
                    break;
                }
            }
            // 日付を更新
            dt = dt.plusDays(1);
        }
    }

    /**
     * 期間中の〇シフトを削除
     * 
     * @param startDt
     * @param endDt
     * @return 削除数
     */
    public int deleteWorkShiftByDateBetween(LocalDate startDt, LocalDate endDt) {
        // 期間中のシフトを取得
        List<WorkShift> shifts = wsRepo.findByDateBetween(startDt, endDt);
        // 削除数カウント
        int count = 0;
        // 〇がのシフトをリポジトリから削除
        for (WorkShift ws : shifts) {
            if (ws.getWork() > 0) {
                wsRepo.delete(ws);
                count++;
            }
        }
        return count;
    }

}
