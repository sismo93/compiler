@.strR = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}
declare i32 @__isoc99_scanf(i8*, ...)
@.strP = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)
  ret void
}
declare i32 @printf(i8*, ...)
define void @defaultFunctionName0(){
%a_0 = alloca i32
store i32 1, i32* %a_0
%b_0 = alloca i32
store i32 100, i32* %b_0
%c_0 = alloca i32
store i32 4, i32* %c_0
br label %whileCond0
whileCond0:
%1 = load i32, i32* %b_0
%2 = icmp ne i32 %1, 0
br i1 %2, label %whileCode0, label %whileRest0
br label %whileCode0
whileCode0:
%4 = load i32, i32* %b_0
%5 = load i32, i32* %a_0
%6 = sub i32 %4, %5
store i32 %6, i32* %b_0
%7 = load i32, i32* %b_0
call void @println(i32 %7)
br label %whileCond7
whileCond7:
%8 = sub i32 0, 5
%9 = load i32, i32* %c_0
%10 = icmp ne i32 %9, %8
br i1 %10, label %whileCode7, label %whileRest7
br label %whileCode7
whileCode7:
%12 = load i32, i32* %c_0
%13 = sub i32 %12, 1
store i32 %13, i32* %c_0
%14 = load i32, i32* %c_0
call void @println(i32 %14)
br label %whileCond7
br label %whileRest7
whileRest7:
br label %whileCond0
br label %whileRest0
whileRest0:
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
