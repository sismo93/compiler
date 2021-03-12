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
define void @P5(){
%v_0 = alloca i32
store i32 6, i32* %v_0
%1 = load i32, i32* %v_0
call void @println(i32 %1)
ret void
}

define void @P6(){
%a_0 = alloca i32
store i32 5, i32* %a_0
%1 = load i32, i32* %a_0
%2 = icmp slt i32 %1, 6
br i1 %2, label %ifCode2, label %elseCode2
br label %ifCode2
ifCode2:
%4 = load i32, i32* %a_0
call void @println(i32 %4)
br label %restCode2
br label %elseCode2
elseCode2:
br label %restCode2
restCode2:
ret void
}

define void @P7(){
%v_0 = alloca i32
store i32 6, i32* %v_0
%1 = load i32, i32* %v_0
%2 = sub i32 0, %1
%3 = add i32 %2, 6
%a_0 = alloca i32
store i32 %3, i32* %a_0
%4 = load i32, i32* %a_0
call void @println(i32 %4)
ret void
}

define void @defaultFunctionName3(){
%a_0 = alloca i32
store i32 4, i32* %a_0
br label %whileCond0
whileCond0:
%1 = load i32, i32* %a_0
%2 = icmp ne i32 %1, 0
br i1 %2, label %whileCode0, label %whileRest0
br label %whileCode0
whileCode0:
%4 = load i32, i32* %a_0
%5 = sub i32 %4, 1
store i32 %5, i32* %a_0
br label %whileCond0
br label %whileRest0
whileRest0:
ret void
}

define void @EmptyP(){
ret void
}

define void @P10(){
%x_0 = alloca i32
store i32 0, i32* %x_0
store i32 0, i32* %x_0
br label %forCond0
forCond0:
%comp0_0 = alloca i32
store i32 5, i32* %comp0_0
%1 = load i32, i32* %x_0
%2 = load i32, i32* %comp0_0
%3 = icmp sgt i32 %2, 0
br i1 %3, label %forward3, label %backward3
br label %forward3
forward3:
%5 = icmp sle i32 %1, %2
br i1 %5, label %forCode0, label %forOut0
backward3:
%6 = icmp sge i32 %1, %2
br i1 %6, label %forCode0, label %forOut0
br label %forCode0
forCode0:
%8 = load i32, i32* %x_0
%9 = add i32 %8, 2
store i32 %9, i32* %x_0
%10 = load i32, i32* %x_0
call void @println(i32 %10)
%addVal10_0 = alloca i32
store i32 1, i32* %addVal10_0
%11 = load i32, i32* %addVal10_0
%12 = load i32, i32* %x_0
%13 = add i32 %12, %11
store i32 %13, i32* %x_0
br label %forCond0
forOut0:
ret void
}

define i32 @main(){
call void @P5()
call void @P6()
call void @P7()
call void @defaultFunctionName3()
call void @EmptyP()
call void @P10()
ret i32 0
}
