package org.tuvisminds.datatables.operations;

import org.tuvisminds.datatables.data.core.DataTable;
import org.tuvisminds.datatables.reader.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;

@SpringBootTest
class TableOperationsTest {
    @Autowired
    DataRepository dataRepository;

    String query = "select case\n" +
            "           when ((TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - (SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '17:30' and '23:59') or\n" +
            "                 (TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - (SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '00:00' and '02:29')) then 'TRUE'\n" +
            "           ELSE 'FALSE' END as night,\n" +
            "       b.BLOCK_START_DATE_TIME,\n" +
            "       p.pattern_name,\n" +
            "       P.PATTERN_ID,\n" +
            "       b.BLOCK_ID,\n" +
            "       csb.CREW_SCHD_BLOCK_ID,\n" +
            "       csb.CREW_PROFILE_ID,\n" +
            "       cp.CREW_TYPE_CODE,\n" +
            "       csb.AUTO_OR_MANUAL,\n" +
            "       p.pattern_type,\n" +
            "       p.base,\n" +
            "       p.NUMBER_OF_BLOCKS,\n" +
            "       p.DISTRIBUTION_IDENTIFIER,\n" +
            "       csb.CREW_SCHEDULE_BLOCK_TYPE,\n" +
            "       csb.IS_CREW_BLUELINE,\n" +
            "       csb.*\n" +
            "from CSS_T_PATTERN p,\n" +
            "     CSS_T_CREW_SCHD_BLOCK csb,\n" +
            "     CSS_T_BLOCK b,\n" +
            "     CSS_T_CREW_PROFILE cp,\n" +
            "     CSS_T_ROSTER_CYCLE rc\n" +
            "where p.PATTERN_ID = b.PATTERN_ID\n" +
            "  and b.BLOCK_ID = csb.BLOCK_ID\n" +
            "  and csb.CREW_PROFILE_ID = cp.CREW_PROFILE_ID\n" +
            "  and rc.CYCLE_ID = :cycleId\n" +
            "  and b.BLOCK_START_DATE_TIME between rc.DATE_FROM and rc.DATE_TO\n" +
            "order by b.PATTERN_ID, CSB.CREW_PROFILE_ID";

    String query2 = "select case\n" +
            "           when ((TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - nvl((SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),1/24),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '17:30' and '23:59') or\n" +
            "                 (TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - nvl((SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),1/24),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '00:00' and '02:29')) then 1\n" +
            "           ELSE 0 END as night,\n" +
            "       case\n" +
            "           when ((TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - nvl((SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),1/24),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '02:30' and '05:29')) then 1\n" +
            "           ELSE 0 END as early_morning,\n" +
            "       case\n" +
            "           when ((TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - nvl((SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),1/24),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '05:30' and '10:59')) then 1\n" +
            "           ELSE 0 END as morning,\n" +
            "       case\n" +
            "           when ((TO_CHAR(CSS_F_calc_lcltimeofbaseap(b.BLOCK_START_DATE_TIME - nvl((SELECT MAX(checkin_offset)\n" +
            "                                                                                FROM (select greatest(ct.CHECK_IN_TIME,\n" +
            "                                                                                                      acc.OUTSTATION_CHECKIN,\n" +
            "                                                                                                      acc.STATION_CHECKIN) as checkin_offset\n" +
            "                                                                                      from CSS_T_CREW_TYPE ct,\n" +
            "                                                                                           CSS_T_AIRCRAFT_CREW_CHECKIN acc\n" +
            "                                                                                      where ct.CREW_TYPE_MST_CODE = acc.CREW_TYPE_MST_CODE\n" +
            "                                                                                        AND CT.CREW_TYPE_CODE = cp.CREW_TYPE_CODE\n" +
            "                                                                                        AND ACC.MODEL_NUMBER in\n" +
            "                                                                                            (select AMM.MODEL_NUMBER\n" +
            "                                                                                             from CSS_T_CREW_SCHD_BLOCK csb1,\n" +
            "                                                                                                  CSS_T_CREW_SCHD_FLIGHT_LEG csfl,\n" +
            "                                                                                                  CSS_T_FLIGHT_LEG fl,\n" +
            "                                                                                                  CSS_T_FLIGHT f,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_CAPACITY AMC,\n" +
            "                                                                                                  CSS_T_AIRCRAFT_MODEL_MAPPING AMM\n" +
            "                                                                                             where csb1.CREW_SCHD_BLOCK_ID = csb.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.CREW_SCHD_BLOCK_ID = csb1.CREW_SCHD_BLOCK_ID\n" +
            "                                                                                               and csfl.FLIGHT_LEG_ID = fl.FLIGHT_LEG_ID\n" +
            "                                                                                               and fl.FLIGHT_ID = f.FLIGHT_ID\n" +
            "                                                                                               AND F.AIRCRAFT_MODEL_CAPACITY_ID = AMC.AIRCRAFT_MODEL_CAPACITY_ID\n" +
            "                                                                                               AND AMC.MODEL_NUMBER_EX = AMM.MODEL_NUMBER_EX))),1/24),\n" +
            "                                                     NULL, 'Y'), 'HH24:MI:SS') between '11:00' and '17:29')) then 1\n" +
            "           ELSE 0 END as afternoon,\n" +
            "       b.block_seq,\n" +
            "       p.DISTRIBUTION_IDENTIFIER,\n" +
            "       b.BLOCK_START_DATE_TIME,\n" +
            "       p.pattern_name,\n" +
            "       P.PATTERN_ID,\n" +
            "       b.BLOCK_ID,\n" +
            "       csb.CREW_SCHD_BLOCK_ID,\n" +
            "       csb.CREW_PROFILE_ID,\n" +
            "       cp.crew_id,\n" +
            "       cp.CREW_TYPE_CODE,\n" +
            "       csb.AUTO_OR_MANUAL,\n" +
            "       p.pattern_type,\n" +
            "       p.base,\n" +
            "       p.NUMBER_OF_BLOCKS,\n" +
            "       csb.CREW_SCHEDULE_BLOCK_TYPE,\n" +
            "       csb.IS_CREW_BLUELINE\n" +
            "from CSS_T_PATTERN p,\n" +
            "     CSS_T_CREW_SCHD_BLOCK csb,\n" +
            "     CSS_T_BLOCK b,\n" +
            "     CSS_T_CREW_PROFILE cp,\n" +
            "     CSS_T_ROSTER_CYCLE rc\n" +
            "where p.PATTERN_ID = b.PATTERN_ID\n" +
            "  and PATTERN_TYPE in ('FLIGHT', 'STANDBY')\n" +
            "  and b.BLOCK_ID = csb.BLOCK_ID\n" +
            "  and csb.CREW_PROFILE_ID = cp.CREW_PROFILE_ID\n" +
            "  and rc.CYCLE_ID = :cycleId\n" +
//            "  and csb.CREW_PROFILE_ID = 4194\n" +
            "  and b.BLOCK_START_DATE_TIME between rc.DATE_FROM and rc.DATE_TO\n" +
            "order by b.PATTERN_ID, CSB.CREW_PROFILE_ID";

    @Test
    void aggregate() throws Exception {
        Map<String, Object> parameters = Collections.singletonMap("cycleId", 199);
        long timeStart = System.currentTimeMillis();
        DataTable columnTable = dataRepository.readData(query2, parameters);
        System.out.println("Data Read:" + (System.currentTimeMillis() - timeStart));
        TableOperations tableOperations = new TableOperations(columnTable);
        DataTable columnTable1 = tableOperations.filterColumns("CREW_PROFILE_ID", "NIGHT")
                .aggregateAndSum("CREW_PROFILE_ID", "NIGHT").getDataTable();
        int i = 0;
    }
}