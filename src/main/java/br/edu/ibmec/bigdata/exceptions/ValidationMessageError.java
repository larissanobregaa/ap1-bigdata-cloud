package br.edu.ibmec.bigdata.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationMessageError {
    private String message = "Há erros na sua requisição, verique";
    private List<ValidationError> errors = new ArrayList<>();
}

