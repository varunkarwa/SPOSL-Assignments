%{
    #include<stdio.h>
    #include<stdlib.h>
%}
%token ID DT SC COMMA NUM EQ

%%
start: DT varlist SC {printf("valid declaration\n");exit(0);};
varlist: ID EQ NUM
        |ID
        |varlist COMMA ID EQ NUM
        |varlist COMMA ID
        ;
%%

int main ()
{
    printf("Enter the exp:");
    yyparse();

    return 0;
}

int yyerror(char *s){
    printf("Error while declaring variable: SYNTAX ERROR\n");
    return 0;
}