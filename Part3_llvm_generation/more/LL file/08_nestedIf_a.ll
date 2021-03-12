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
store i32 112, i32* %a_0
%b_0 = alloca i32
store i32 2, i32* %b_0
%1 = load i32, i32* %a_0
%2 = icmp sgt i32 %1, 20
br i1 %2, label %ifCode2, label %elseCode2
br label %ifCode2
ifCode2:
%4 = load i32, i32* %a_0
call void @println(i32 %4)
%5 = load i32, i32* %b_0
%6 = mul i32 %5, 545
%7 = load i32, i32* %a_0
%8 = icmp sgt i32 %7, %6
br i1 %8, label %ifCode8, label %elseCode8
br label %ifCode8
ifCode8:
%10 = load i32, i32* %b_0
call void @println(i32 %10)
br label %restCode8
br label %elseCode8
elseCode8:
%12 = load i32, i32* %b_0
%13 = icmp eq i32 %12, 2
br i1 %13, label %ifCode13, label %elseCode13
br label %ifCode13
ifCode13:
%15 = load i32, i32* %a_0
%16 = icmp slt i32 %15, 5454
br i1 %16, label %ifCode16, label %elseCode16
br label %ifCode16
ifCode16:
%c_0 = alloca i32
store i32 1000, i32* %c_0
%18 = load i32, i32* %c_0
call void @println(i32 %18)
br label %restCode16
br label %elseCode16
elseCode16:
br label %restCode16
restCode16:
br label %restCode13
br label %elseCode13
elseCode13:
br label %restCode13
restCode13:
br label %restCode8
restCode8:
br label %restCode2
br label %elseCode2
elseCode2:
br label %restCode2
restCode2:
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
ret i32 0
}
