# OperationPrecedenceParser

This project is about parsing and evaluating aritmetic expressions. The project does not cover all cases but covers:

 - operator + and *
 - integer variables
 - paranthesis

To deal with presence, Shunting Yard Algorithm is used.

It has a UI to get input and show results: 
 - You enter expressions formed by letters.
    ex : (a + b) * c
          a + b * c
 - It checks whether the expression is valid. If it is valid, it requires you to enter corresponding values and shows result. If it is not, it gives warning. 
 
