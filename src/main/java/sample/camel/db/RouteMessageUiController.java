/*
 * Copyright 2025 Mitch Trachtenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel.db;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ui/db")
public class RouteMessageUiController {

    private final RouteMessageService service;

    public RouteMessageUiController(RouteMessageService service) {
        this.service = service;
    }

    @GetMapping("/counts")
    public String showCounts(Model model) {
        model.addAttribute("counts", service.getCountPerRoute());
        return "counts";
    }

    @GetMapping("/messages")
    public String showMessages(@RequestParam String routeId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                               Model model) {
        List<RouteMessage> messages = service.getMessagesByRouteAndTime(routeId, from, to);
        model.addAttribute("messages", messages);
        return "messages";
    }
}
