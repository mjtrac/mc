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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface RouteMessageRepository extends JpaRepository<RouteMessage, Long> {

    @Query("SELECT rm.routeId, COUNT(rm) FROM RouteMessage rm GROUP BY rm.routeId")
    List<Object[]> countMessagesByRoute();

    List<RouteMessage> findByRouteIdAndTimestampProcessedBetween(String routeId, LocalDateTime start, LocalDateTime end);
}
