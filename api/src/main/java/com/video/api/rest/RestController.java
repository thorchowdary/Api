package com.video.api.rest;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/video")
public class RestController {
    // Map to store replayed sections data
    private static final Map<String, Map<String, List<Map<String, Integer>>>> replayedSections = new HashMap<>();

    @PostMapping("/track_replayed_section")
    public String trackReplayedSection(@RequestBody Map<String, Object> requestData) {
        String userId = (String) requestData.get("user_id");
        String videoId = (String) requestData.get("video_id");
        int startTime = (int) requestData.get("start_time");
        int endTime = (int) requestData.get("end_time");

        if (!requestData.containsKey("user_id") || !requestData.containsKey("video_id") ||
                !requestData.containsKey("start_time") || !requestData.containsKey("end_time")) {
            return "Incomplete data provided";
        }

        replayedSections.computeIfAbsent(userId, k -> new HashMap<>());
        replayedSections.get(userId).computeIfAbsent(videoId, k -> new ArrayList<>());
        replayedSections.get(userId).get(videoId).add(Map.of("start_time", startTime, "end_time", endTime));

        return "Replayed section tracked successfully";
    }

    @GetMapping("/get_replayed_sections/{userId}/{videoId}")
    public List<Map<String, Integer>> getReplayedSections(@PathVariable String userId, @PathVariable String videoId) {
        if (!replayedSections.containsKey(userId) || !replayedSections.get(userId).containsKey(videoId)) {
            return List.of();
        }

        return replayedSections.get(userId).get(videoId);
    }

}
