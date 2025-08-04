package com.cooper_filme.shared_model.util;

import com.cooper_filme.shared_model.model.ScriptStatus;

public final class StatusTranslator {
    private StatusTranslator(){
    }

    public static ScriptStatus translateStringToStatus(String status){
        switch (status.toLowerCase()){
            case "aguardando_analise": return ScriptStatus.AWAITING_ANALYSIS;
            case "em_analise": return ScriptStatus.UNDER_ANALYSIS;
            case "aguardando_revisao": return ScriptStatus.AWAITING_REVIEW;
            case "em_revisão": return ScriptStatus.UNDER_REVIEW;
            case "aguardando_aprovacao": return ScriptStatus.WAITING_FOR_APPROVAL;
            case "em_aprovacao": return ScriptStatus.IN_APPROVAL;
            case "aprovado": return ScriptStatus.APPROVED;
            case "recusado": return ScriptStatus.REJECTED;
            default: throw new UnsupportedOperationException("Doesn't exist this status " + status);
        }
    }

    public static String translateStatusToString(ScriptStatus status){
        switch (status){
            case ScriptStatus.AWAITING_ANALYSIS: return "aguardando_analise";
            case ScriptStatus.UNDER_ANALYSIS: return "em_analise";
            case ScriptStatus.AWAITING_REVIEW: return "aguardando_revisao";
            case ScriptStatus.UNDER_REVIEW: return "em_revisão";
            case ScriptStatus.WAITING_FOR_APPROVAL: return "aguardando_aprovacao";
            case ScriptStatus.IN_APPROVAL: return "em_aprovacao";
            case ScriptStatus.APPROVED: return "aprovado";
            case ScriptStatus.REJECTED: return "recusado";
            default: throw new UnsupportedOperationException("Doesn't exist this status " + status);
        }
    }
}
