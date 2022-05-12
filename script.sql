-- C:\class\java\JDBCTest\script.sql

-- 주소록 테이블
select * from tblAddress;

create table tblAddress(
    seq number primary key,                                 --PK
    name varchar2(10) not null,                             --이름
    age number(3) not null check (age between 0 and 150),   --나이
    gender char(1) not null check(gender in ('m', 'f')),    --성별(m,f)
    tel varchar2(15) not null,                              --전화번호
    address varchar2(300) not null,                         --주소
    regdate date default sysdate not null                   --등록일
    

);


create sequence seqAddress;

insert into tblAddress (seq, name, age, gender, tel, address, regdate)
    values(seqAddress.nextVal, '홍길동', 20, 'm', '010-1234-5678', '서울시 강남구 역삼동 한독빌딩', default);

update tblAddress set age = age + 1 where seq = 1;


select * from tblAddress;

commit;


select * from tblInsa;

select * from tblBonus;

create sequence seqBonus;








-- Ex06_CallableStatement.java

-- m1. 인자값, 반환값
create or replace procedure procM1
is
begin
    insert into tblAddress (seq, name, age, gender, tel, address, regdate)
    values (seqAddress.nextVal, '홍길동', 20, 'm', '010-1234-5678', '서울시 강남구 역삼동 한독빌딩', default);
    
end;



-- m2. 인자값(O), 반환값(X)
create or replace procedure procM2 (
    pname       tblAddress.name%type,
    page        tblAddress.age%type,
    pgender     tblAddress.gender%type,
    ptel        tblAddress.tel%type,
    paddress    tblAddress.address%type
)

is
begin

    insert into tblAddress(seq, name, age, gender, tel, address, regdate)
    values (seqAddress.nextVal, pname, page, pgender, ptel, paddress, default);
end;


-- 검증!!!!
begin
    procM2('이름', 20, 'm', '010-1111-1111', '주소');
end;

select * from tblAddress order by seq desc;





--m3. 인자값(X), 반환값(O)
create or replace procedure procM3 (
    pcount out number
)
is
begin

    select count(*) into pcount from tblAddress;
    
end procM3;

set serverout on;

-- 검증
declare
    vcount number;
begin
    procM3(vcount);
    dbms_output.put_line(vcount);
end;


--m4
--		// 문제. 직원 번호를 입력하면 이름, 부서, 직위, 지역을 반환 + 출력
--		// Oracle Developer에서 프로시저 생성 : procM4 
--		// - in 파라미터 > 직원 번호
--		// - out 파라미터> 이름
--		// - out 파라미터> 부서
--		// - out 파라미터> 직위
--		// - out 파라미터> 지역
create or replace procedure procM4 (
    pnum tblInsa.num%type,
    pname out varchar2,
    pbuseo out varchar2,
    pjikwi out varchar2,
    pcity out varchar2
)
is
begin
    select name, buseo, jikwi, city into pname, pbuseo, pjikwi, pcity 
        from tblInsa where num = pnum;
    
end;   


-- 검증
declare
    vname varchar2;
    vbuseo varchar2;
    vjikwi varchar2;
    vcity varchar2;
begin
    procM4(1001, vname, vbuseo, vjikwi, vcity);
    dbms_output.put_line(vname);
    dbms_output.put_line(vbuseo);
    dbms_output.put_line(vjikwi);
    dbms_output.put_line(vcity);
end;





--m5.
create or replace procedure procM5(
    pbuseo in varchar2,
    pcursor out sys_refcursor -- 커서 생성
)
is
begin

    open pcursor
    for 
        select name, jikwi, basicpay 
            from tblInsa 
                where buseo = pbuseo;

end procM5;


-- 검증
declare
    pcursor sys_refcursor;
    vname tblInsa.name%type;
    vjikwi tblInsa.jikwi%type;
    vbasicpay tblInsa.basicpay%type;
begin
    procM5('개발부', pcursor);
    
    loop
        fetch pcursor into vname, vjikwi, vbasicpay;
        exit when pcursor%notfound;
        
        dbms_output.put_line(vname || ',' || vjikwi || ',' || vbasicpay);
    end loop;
end;




--m6
create or replace procedure procM6(
    pbuseo in varchar2,
    pcursor out sys_refcursor
)
is
        
begin

    --1. 부서명 > 직원 번호 
    
    --2. 직원번호 > 보너스 지급 내역
    open pcursor for select name, bonus from tblInsa i
                    left outer join tblBonus b
                        on i.num = b.num
                            where buseo = pbuseo;

end procM6;

declare
    vname varchar2(30);
    vbonus number;
    vcursor sys_refcursor;
begin
    procM6('기획부', vcursor);
    
    loop
        fetch vcursor into vname, vbonus;
        exit when vcursor%notfound;
        
        dbms_output.put_line(vname || ' > ' || vbonus);
    end loop;
    
end;



















