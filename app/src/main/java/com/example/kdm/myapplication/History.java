package com.example.kdm.myapplication;

public class History {

    String expression;
    int result;
    int count;

    public History() {
    }

    public History(String expression, int result, int count) {
        this.expression = expression;
        this.result = result;
        this.count = count;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
