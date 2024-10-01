package br.edu.ibmec.bigdata.exceptions;

import lombok.Data;

@Data
public class ValidationError {
        private String field;
        private String message;
    }

