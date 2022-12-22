package com.lab.it;

import com.lab.it.database.DatabaseHandler;
import com.lab.it.database.attribute.AttributeHandler;
import com.lab.it.table.TableHandler;
import com.lab.it.table.column.ColumnHandler;
import com.lab.it.table.row.RowHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ApplicationConfig implements WebFluxConfigurer {
    @Bean
    public RouterFunction<ServerResponse> serverApi(DatabaseHandler databaseHandler,
                                                    TableHandler tableHandler,
                                                    ColumnHandler columnHandler,
                                                    RowHandler rowHandler,
                                                    AttributeHandler attributeHandler) {
        return route(POST("/databases"), databaseHandler::createDatabase)
                .andRoute(GET("/databases"), databaseHandler::getDatabases)
                .andRoute(GET("/databases/{databaseId}"), databaseHandler::getDatabase)
                .andRoute(PUT("/databases/{databaseId}"), databaseHandler::updateDatabase)
                .andRoute(DELETE("/databases/{databaseId}"), databaseHandler::deleteDatabase)
                .andRoute(POST("/databases/{databaseId}/tables"), tableHandler::createTable)
                .andRoute(GET("/databases/{databaseId}/tables"), tableHandler::getTables)
                .andRoute(GET("/databases/{databaseId}/tables/{tableId}"), tableHandler::getTable)
                .andRoute(PUT("/databases/{databaseId}/tables/{tableId}"), tableHandler::updateTable)
                .andRoute(DELETE("/databases/{databaseId}/tables/{tableId}"), tableHandler::deleteTable)
                .andRoute(POST("/databases/{databaseId}/tables/{tableId}/columns"), columnHandler::createColumn)
                .andRoute(GET("/databases/{databaseId}/tables/{tableId}/columns"), columnHandler::getColumns)
                .andRoute(GET("/databases/{databaseId}/tables/{tableId}/columns/{columnId}"), columnHandler::getColumn)
                .andRoute(PUT("/databases/{databaseId}/tables/{tableId}/columns/{columnId}"), columnHandler::updateColumn)
                .andRoute(DELETE("/databases/{databaseId}/tables/{tableId}/columns/{columnId}"), columnHandler::deleteColumn)
                .andRoute(POST("/databases/{databaseId}/tables/{tableId}/rows"), rowHandler::createRow)
                .andRoute(GET("/databases/{databaseId}/tables/{tableId}/rows"), rowHandler::getRows)
                .andRoute(GET("/databases/{databaseId}/tables/{tableId}/rows/{rowId}"), rowHandler::getRow)
                .andRoute(DELETE("/databases/{databaseId}/tables/{tableId}/rows/{rowId}"), rowHandler::deleteRow)
                .andRoute(POST("/databases/{databaseId}/attributes"), attributeHandler::createAttribute)
                .andRoute(GET("/databases/{databaseId}/attributes"), attributeHandler::getAttributes)
                .andRoute(GET("/databases/{databaseId}/attributes/{attributeId}"), attributeHandler::getAttribute)
                .andRoute(PUT("/databases/{databaseId}/attributes/{attributeId}"), attributeHandler::updateAttribute)
                .andRoute(DELETE("/databases/{databaseId}/attributes/{attributeId}"), attributeHandler::deleteAttribute);
    }
}
