// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.admin.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.alert.Alert;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.response.AlertResponse;
import org.apache.cloudstack.api.response.ListResponse;
import com.cloud.utils.Pair;

@Implementation(description = "Lists all alerts.", responseObject = AlertResponse.class)
public class ListAlertsCmd extends BaseListCmd {

    public static final Logger s_logger = Logger.getLogger(ListAlertsCmd.class.getName());

    private static final String s_name = "listalertsresponse";

    // ///////////////////////////////////////////////////
    // ////////////// API parameters /////////////////////
    // ///////////////////////////////////////////////////

    @IdentityMapper(entityTableName="alert")
    @Parameter(name = ApiConstants.ID, type = CommandType.LONG, description = "the ID of the alert")
    private Long id;

    @Parameter(name = ApiConstants.TYPE, type = CommandType.STRING, description = "list by alert type")
    private String type;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    // ///////////////////////////////////////////////////
    // ///////////// API Implementation///////////////////
    // ///////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public void execute() {
        Pair<List<? extends Alert>, Integer> result = _mgr.searchForAlerts(this);
        ListResponse<AlertResponse> response = new ListResponse<AlertResponse>();
        List<AlertResponse> alertResponseList = new ArrayList<AlertResponse>();
        for (Alert alert : result.first()) {
            AlertResponse alertResponse = new AlertResponse();
            alertResponse.setId(alert.getUuid());
            alertResponse.setAlertType(alert.getType());
            alertResponse.setDescription(alert.getSubject());
            alertResponse.setLastSent(alert.getLastSent());

            alertResponse.setObjectName("alert");
            alertResponseList.add(alertResponse);
        }

        response.setResponses(alertResponseList, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
