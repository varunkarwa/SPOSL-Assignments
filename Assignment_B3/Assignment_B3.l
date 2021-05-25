%{
int line=0, s_chars=0, c_chars=0, words=0, sp_chars=0, num=0, total=0;
%}

%%
["\n"]      {line++;words++;}
[\t" "]      {words++;}
[a-z]       {s_chars++;}
[A-Z]       {c_chars++;}
[,;:#@!.]       {sp_chars++;}
[0-9]       {num++;}
%%

void main()
{
FILE *fp;
fp = fopen("B3.txt","r");
yyin = fp;
yylex();
total = s_chars + c_chars + sp_chars;
printf("Number of lines: %d",line);printf("\n");
printf("Number of words: %d",words);printf("\n");
printf("Number of total: %d",total);printf("\n");
}

int yywrap(){
    return 1;
}